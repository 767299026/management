<template>
  <div id="user">
    <!--面包屑导航栏-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片视图区域-->
    <el-card>
      <el-row :gutter='20'>
        <el-col :span="7">
          <el-input v-model="queryInfo.query" placeholder="根据昵称查找用户" clearable @clear="getUserList">
            <el-button slot="append" icon="el-icon-search" @click="getUserList"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addUserDialogVisible = true">添加用户</el-button>
        </el-col>
      </el-row>
      <!--用户列表区域-->
      <el-table :data="currentPageData">
        <el-table-column type="index"/>
        <el-table-column label="昵称" prop="username"/>
        <el-table-column label="邮箱" prop="email"/>
        <el-table-column label="头像">
          <template v-slot="scope">
            <img :src="scope.row.avatar" alt="头像" width="50px" height="50px" class="el-avatar--large">
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime"/>
        <el-table-column label="修改时间" prop="updateTime"/>
        <el-table-column label="用户角色" prop="role"/>
        <el-table-column label="是否禁用">
          <template v-slot="scope">
            <el-switch v-model="scope.row.status" :active-value="0" :inactive-value="1"
                       @change="changeStatus(scope.row.id)"/>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200px">
          <template v-slot="scope">
            <!--修改按钮-->
            <el-button type="primary" icon="el-icon-edit" size="mini" title="编辑"
                       @click="showEditUserDialogVisible(scope.row.id)"/>
            <!--删除按钮-->
            <el-button type="danger" icon="el-icon-delete" size="mini" title="删除"
                       @click="confirmDeleteUser(scope.row.id)"/>
            <!--分配角色按钮-->
            <el-button type="warning" icon="el-icon-setting" size="mini" title="分配角色"
                       @click="showDistributeDialogVisible(scope.row)"/>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryInfo.currentPage"
          :page-sizes="[1, 2, 4]"
          :page-size="queryInfo.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalNums">
      </el-pagination>
    </el-card>

    <!--添加用户对话框-->
    <el-dialog title="添加用户" :visible.sync="addUserDialogVisible" width="50%" @click="resetAddUserForm()">
      <el-form :model="addForm" :rules="addFormRules" ref="addUserFormRef" label-width="70px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addForm.username"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addForm.email" @blur="checkEmailFormat"/>
        </el-form-item>
        <el-form-item label="是否禁用" prop="status">
          <el-switch v-model="addForm.status" :active-value="0" :inactive-value="1"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addUserDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addUser()">确 定</el-button>
      </span>
    </el-dialog>

    <!--编辑用户对话框-->
    <el-dialog title="编辑用户" :visible.sync="editUserDialogVisible" width="50%" @click="resetEditUserForm()">
      <el-form :model="editForm" :rules="editFormRules" ref="editUserFormRef" label-width="70px">
        <el-form-item label="userId">
          <el-input v-model="editForm.id" disabled/>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="editForm.username"/>
        </el-form-item>
        <el-form-item label="头像">
          <img :src="editForm.avatar" alt="头像" width="150px" height="150px">
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" @blur="checkEmailFormat"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editUserDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editUser()">确 定</el-button>
      </span>
    </el-dialog>

    <!--分配角色对话框-->
    <el-dialog title="分配角色" :visible.sync="distributeDialogVisible" width="50%"
               @close="userInfo = {};roleList = [];selectedRoleId = ''">
      <div>
        <p>当前的用户：{{ userInfo.username }}</p>
        <p>当前的角色：{{ userInfo.role }}</p>
        <p>分配角色：
          <el-select v-model="selectedRoleId" placeholder="请选择">
            <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleName" :value="item.roleId"/>
          </el-select>
        </p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="distributeDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmAllotRole()">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "user",
  created() {
    this.getUserList()
  },
  data() {
    return {
      queryInfo: {//查询当页用户列表的查询参数
        query: '',
        currentPage: 1,
        pageSize: 4
      },
      currentPageData: [],//当页用户列表数据
      totalNums: 0,//后台返回的用户总条数

      addUserDialogVisible: false,//是否打开添加用户对话框
      addForm: {//添加用户表单信息
        username: '',
        password: '',
        email: '',
        status: '1'
      },
      addFormRules: {//添加用户表单验证规则
        username: [
            { required: true, message: '请输入正确的用户名', trigger: 'blur'},
            { min: 3, max: 12, message: "长度在3-12个字符", trigger: "blur"}
        ],
        password: [
          { required: true, message: "请输入正确的密码", trigger: "blur"},
          { min: 6, max: 15, message: "长度在6-15个字符", trigger: "blur"}
        ],
        email: [
          { required: true, message: '请输入正确的邮箱格式', trigger: 'blur'}
        ]
      },
      editUserDialogVisible: false,//是否打开添加用户对话框
      editForm: {//编辑用户信息表单信息
        id: 0,
        username: '',
        email: '',
        avatar: '',
      },
      editFormRules: {//编辑用户信息表单验证规则
        username: [
          {required: true, message: '请输入正确的用户名', trigger: 'blur'},
          {min: 3, max: 12, message: "长度在3-12个字符", trigger: "blur"}
        ],
        email: [
          {required: true, message: '请输入正确的邮箱格式', trigger: 'blur'}
        ]
      },
      distributeDialogVisible: false,//分配角色对话框
      roleList: [],//已有的角色列表
      userInfo: {},//分配角色的用户当前信息
      selectedRoleId: ''
    }
  },
  methods: {
    getUserList() {//axios分页获取用户列表请求
      this.$axios.getUerList(this.queryInfo).then((res) => {
        this.currentPageData = res.data.data.records
        this.totalNums = res.data.data.total
        Message.success(res.data.msg)
      })
    },
    handleSizeChange(newSize) {//设置分页请求的页大小
      this.queryInfo.pageSize = newSize
      this.getUserList()
    },
    handleCurrentChange(newPage) {//设置分页请求的页标
      this.queryInfo.currentPage = newPage
      this.getUserList()
    },
    changeStatus(userId) {//axios更改用户状态请求
      this.$axios.changeUserStatus(userId).then((res) => {
        Message.success(res.data.msg)
      })
    },
    checkEmailFormat: function() {//正则校验邮箱格式
      const regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (this.addForm.email !== '' && !regEmail.test(this.addForm.email)) {
        Message.error("邮箱格式不正确")
        this.addForm.email = ''
      }
    },
    resetAddUserForm() {//重置添加用户对话框数据
      this.$refs.addUserFormRef.resetFields()
    },
    addUser() {//axios添加用户请求
      this.$refs.addUserFormRef.validate(valid => {
        if (!valid) return
        this.$axios.addUser(this.addForm).then((res) => {
          this.addUserDialogVisible = false
          this.getUserList()
          Message.success(res.data.msg)
        })
      })
    },
    resetEditUserForm() {//重置编辑用户对话框数据
      if (this.$refs.editUserFormRef !== undefined)
        this.$refs.editUserFormRef.resetFields()
    },
    showEditUserDialogVisible(userId) {//axios获取用户对话框信息
      this.resetEditUserForm()
      this.$axios.getUserInfo(userId).then((res) => {
        this.editForm = res.data.data
        this.editUserDialogVisible = true
        Message.success(res.data.msg)
      })
    },
    editUser() {//axios编辑用户信息请求
      this.$refs.editUserFormRef.validate(valid => {
        if (!valid) return
        this.$axios.editUser(this.editForm).then((res) => {
          this.editUserDialogVisible = false
          this.getUserList()
          Message.success(res.data.msg)
        })
      })
    },
    confirmDeleteUser(id) {//删除用户操作
      this.$confirm("该操作不可逆，确认移除该用户？", '删除操作', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.deleteUser(id).then((res) => {
          this.getUserList()
          Message.success(res.data.msg)
        })
      }).catch(action => {
        if (action === 'cancel') {
          Message.info("取消了移除操作")
        }
      })
    },
    showDistributeDialogVisible(userInfo) {//分配角色对话框
      this.userInfo = userInfo
      this.$axios.getRolesName().then((res) => {
        this.roleList = res.data.data
      })
      this.distributeDialogVisible = true
    },
    confirmAllotRole() {
      this.$confirm("确认分配给该用户此角色？", '分配角色', {
        confirmButtonText: '确认',
        cancelButtonText: '再想想',
        type: 'warning'
      }).then(() => {
        if (!this.selectedRoleId)
          return Message.error("请选择分配的角色")
        this.$axios.allotRole(this.userInfo, this.selectedRoleId).then((res) => {
          this.getUserList()
          this.distributeDialogVisible = false
          Message.success(res.data.msg)
        })
      }).catch(action => {
        if (action === 'cancel') {
          Message.info("取消了分配角色")
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.el-switch.is-disabled .el-switch__core,
.el-switch.is-disabled .el-switch__label{
  cursor: pointer !important;
}
</style>
<template>
  <div id="user">
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
          <template slot-scope="scope">
            <img :src="scope.row.avatar" alt="头像" width="50px" height="50px" class="el-avatar--large" >
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime"/>
        <el-table-column label="修改时间" prop="updateTime"/>
        <el-table-column label="用户权限" prop="role"/>
        <el-table-column label="是否禁用">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status" :active-value="0" :inactive-value="1" @change="changeStatus(scope.row.id)"/>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <!--修改按钮-->
            <el-button type="primary" icon="el-icon-edit" size="mini" title="编辑" @click="showEditUserDialogVisible(scope.row.id)"/>
            <!--删除按钮-->
            <el-button type="danger" icon="el-icon-delete" size="mini" title="删除" @click="confirmDelete(scope.row.id)"/>
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
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="70px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addForm.username"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addForm.email" @blur="checkEmailFormat"/>
        </el-form-item>
        <el-form-item label="是否设置为管理员" label-width="125px" prop="admin">
          <el-switch v-model="addForm.admin" :active-value="true" :inactive-value="false"/>
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
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="70px">
        <el-form-item label="用户id">
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
        <el-form-item label="是否设置为管理员" label-width="125px" prop="role">
          <el-switch v-model="editForm.role" :active-value="'root'" :inactive-value="'visitor'" @click.native="confirmPermission(editForm.role)"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editUserDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editUser()">确 定</el-button>
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
        admin: false,
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
        role: ''
      },
      editFormRules: {//编辑用户信息表单验证规则
        username: [
          { required: true, message: '请输入正确的用户名', trigger: 'blur'},
          { min: 3, max: 12, message: "长度在3-12个字符", trigger: "blur"}
        ],
        email: [
          { required: true, message: '请输入正确的邮箱格式', trigger: 'blur'}
        ]
      }
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
      this.$refs.addFormRef.resetFields()
    },
    addUser() {//axios添加用户请求
      this.$refs.addFormRef.validate(valid => {
        if (!valid) return
        this.$axios.addUser(this.addForm).then((res) => {
          this.addUserDialogVisible = false
          this.getUserList()
          Message.success(res.data.msg)
        })
      })
    },
    resetEditUserForm() {//重置编辑用户对话框数据
      if (this.$refs.editFormRef !== undefined)
        this.$refs.editFormRef.resetFields()
    },
    confirmPermission(role) {//提示编辑用户权限操作警告
      const strOpen = '正在执行权限操作，确认赋予该用户管理员权限？'
      const strClose = '正在执行权限操作，确认取消该用户管理员权限？'
      if(role === 'root') {
        this.$confirm(strOpen,'提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.editForm.role = 'root'
        }).catch(action => {
          if (action === 'cancel'){
            this.editForm.role = 'visitor'
          }
        })
      } else {
        this.$confirm(strClose,'提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.editForm.role = 'visitor'
        }).catch(action => {
          if (action === 'cancel'){
            this.editForm.role = 'root'
          }
        })
      }
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
      this.$refs.editFormRef.validate(valid => {
        if (!valid) return
        this.$axios.editUser(this.editForm).then((res) => {
          this.editUserDialogVisible = false
          this.getUserList()
          Message.success(res.data.msg)
        })
      })
    },
    confirmDelete(id) {//删除用户操作
      this.$confirm("该操作不可逆，确认移除该用户？",'删除操作',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.deleteUser(id).then((res) => {
          this.getUserList()
          Message.success(res.data.msg)
        })
      }).catch(action => {
        if (action === 'cancel'){
          Message.info("取消了移除操作")
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
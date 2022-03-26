<template>
  <div id="role">
    <!--面包屑导航栏-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item>角色列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片视图区域-->
    <el-card>
      <!--添加角色按钮区域-->
      <el-row>
        <el-col>
          <el-button type="primary" @click="addRoleDialogVisible = true">添加角色</el-button>
        </el-col>
      </el-row>
      <!--角色列表区域-->
      <el-table :data="roleList" border stripe>
        <el-table-column type="expand">
          <template v-slot="scope">
            <el-row :class="['borderBottom','vcenter',indexOne === 0 ? 'borderHeader' : '']"
                    v-for="(itemLevelOne,indexOne) in scope.row.children" :key="itemLevelOne.roleId">
              <!--一级权限-->
              <el-col :span="5">
                <el-tag size="medium">{{ itemLevelOne.authName }}</el-tag>
                <i class="el-icon-caret-right"/>
              </el-col>
              <!--二级三级权限-->
              <el-col :span="19">
                <el-row :class="['vcenter',indexTwo === 0 ? '' : 'borderHeader']"
                        v-for="(itemLevelTwo,indexTwo) in itemLevelOne.children" :key="itemLevelTwo.roleId">
                  <el-col :span="6">
                    <el-tag size="medium" type="success">{{ itemLevelTwo.authName }}</el-tag>
                    <i class="el-icon-caret-right"/>
                  </el-col>
                  <el-col :span="18">
                    <el-tag type="warning" v-for="(itemLevelThree,indexThree) in itemLevelTwo.children"
                            :key="itemLevelThree.roleId">
                      {{ itemLevelThree.authName }}
                    </el-tag>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
        <el-table-column type="index"/>
        <el-table-column label="角色名称" prop="roleName"/>
        <el-table-column label="角色描述" prop="roleDesc"/>
        <el-table-column label="操作" width="300px">
          <template v-slot="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit"
                       @click="showEditRoleDialogVisible(scope.row.roleId)">编辑
            </el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="confirmDeleteRole(scope.row.roleId)">删除
            </el-button>
            <el-button size="mini" type="warning" icon="el-icon-setting"
                       @click="showDistributeDialogVisible(scope.row)">分配权限
            </el-button>
          </template>
        </el-table-column>

      </el-table>
    </el-card>

    <!--添加角色对话框-->
    <el-dialog title="添加用户" :visible.sync="addRoleDialogVisible" width="50%" @click="resetAddRoleForm()">
      <el-form :model="addForm" :rules="addFormRules" ref="addRoleFormRef" label-width="70px">
        <el-form-item label="角色名称" label-width="80px" prop="roleName">
          <el-input v-model="addForm.roleName"/>
        </el-form-item>
        <el-form-item label="角色描述" label-width="80px" prop="roleDesc">
          <el-input v-model="addForm.roleDesc"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addRoleDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addRole()">确 定</el-button>
      </span>
    </el-dialog>

    <!--编辑角色对话框-->
    <el-dialog title="编辑用户" :visible.sync="editRoleDialogVisible" width="50%" @click="resetEditRoleForm()">
      <el-form :model="editForm" :rules="editFormRules" ref="editRoleFormRef" label-width="70px">
        <el-form-item label="roleId" prop="roleName">
          <el-input v-model="editForm.roleId" disabled/>
        </el-form-item>
        <el-form-item label="角色名称" label-width="80px" prop="roleName">
          <el-input v-model="editForm.roleName"/>
        </el-form-item>
        <el-form-item label="角色描述" label-width="80px" prop="roleDesc">
          <el-input v-model="editForm.roleDesc"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editRoleDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editRole()">确 定</el-button>
      </span>
    </el-dialog>

    <!--分配权限对话框-->
    <el-dialog title="分配权限" :visible.sync="distributeDialogVisible" width="50%" @close="defKeys = [];allotRoleId = 0">
      <el-tree :data="rightsTree" :props="treeProps" show-checkbox node-key="id" default-expand-all
               :default-checked-keys="defKeys" ref="treeRef"></el-tree>
      <span slot="footer" class="dialog-footer">
        <el-button @click="distributeDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmAllotRights()">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "roles",
  created() {
    this.getRoles()
  },
  data() {
    return {
      roleList: [],
      addRoleDialogVisible: false,//是否打开添加角色对话框
      addForm: {//添加角色表单信息
        roleName: '',
        roleDesc: '',
      },
      addFormRules: {//添加角色表单验证规则
        roleName: [
          {required: true, message: '角色名不能为空', trigger: 'blur'},
          {max: 12, message: "名称过长！", trigger: "blur"}
        ],
        roleDesc: [
          {required: true, message: "角色描述不能为空", trigger: "blur"},
          {max: 20, message: "描述过长", trigger: "blur"}
        ]
      },
      editRoleDialogVisible: false,//是否打开编辑角色对话框
      editForm: {//编辑角色表单信息
        roleId: 0,
        roleName: '',
        roleDesc: '',
      },
      editFormRules: {//编辑角色表单验证规则
        roleName: [
          {required: true, message: '角色名不能为空', trigger: 'blur'},
          {max: 12, message: "名称过长！", trigger: "blur"}
        ],
        roleDesc: [
          {required: true, message: "角色描述不能为空", trigger: "blur"},
          {max: 20, message: "描述过长", trigger: "blur"}
        ]
      },
      distributeDialogVisible: false,//是否打开编辑角色对话框
      rightsTree: [],//树状权限列表
      treeProps: {//树状权限列表的属性
        label: 'authName',//显示名称
        children: 'children'//子树
      },
      defKeys: [],//选中的节点的Id值
      allotRoleId: 0
    }
  },
  methods: {
    getRoles() {//获取角色列表
      this.$axios.getRoles().then((res) => {
        this.roleList = res.data.data
        Message.success(res.data.msg)
      })
    },
    resetAddRoleForm() {//重设添加角色表单
      this.$refs.addRoleFormRef.resetFields()
    },
    addRole() {//axios添加角色请求
      this.$refs.addRoleFormRef.validate(valid => {
        if (!valid) return
        this.$axios.addRoles(this.addForm).then((res) => {
          this.addUserDialogVisible = false
          this.getRoles()
          Message.success(res.data.msg)
        })
      })
    },
    resetEditRoleForm() {//重设编辑角色表单
      if (this.$refs.editRoleFormRef !== undefined)
        this.$refs.editRoleFormRef.resetFields()
    },
    showEditRoleDialogVisible(roleId) {//axios获取角色对话框信息
      this.resetEditRoleForm()
      this.$axios.getRoleInfo(roleId).then((res) => {
        this.editForm = res.data.data
        this.editRoleDialogVisible = true
        Message.success(res.data.msg)
      })
    },
    editRole() {//axios编辑角色信息请求
      this.$refs.editRoleFormRef.validate(valid => {
        if (!valid) return
        this.$axios.editRoles(this.editForm).then((res) => {
          this.editRoleDialogVisible = false
          this.getRoles()
          Message.success(res.data.msg)
        })
      })
    },
    confirmDeleteRole(id) {//删除角色操作
      this.$confirm("该操作不可逆，确认移除该角色？", '删除操作', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.deleteRoles(id).then((res) => {
          this.getRoles()
          Message.success(res.data.msg)
        })
      }).catch(action => {
        if (action === 'cancel') {
          Message.info("取消了移除操作")
        }
      })
    },
    showDistributeDialogVisible(role) {//分配权限对话框
      this.allotRoleId = role.roleId
      this.$axios.getRightsTree().then((res) => {
        this.rightsTree = res.data.data
      })
      this.getLeafKeys(role, this.defKeys)
      this.distributeDialogVisible = true
    },
    getLeafKeys(node, arr) {//获取三级权限
      if (!node.children)//不含children则为三级权限
        return arr.push(node.id)
      node.children.forEach(item => this.getLeafKeys(item, arr))
    },
    confirmAllotRights() {//确认分配权限
      const keys = [
        ...this.$refs.treeRef.getCheckedKeys(),
        ...this.$refs.treeRef.getHalfCheckedKeys()
      ]
      const rights = keys.join(",")
      this.$confirm("确认分配给该角色以下权限？", '分配权限', {
        confirmButtonText: '确认',
        cancelButtonText: '再想想',
        type: 'warning'
      }).then(() => {
        this.$axios.allotRights(this.allotRoleId, rights).then((res) => {
          this.getRoles()
          this.distributeDialogVisible = false
          Message.success(res.data.msg)
        })
      }).catch(action => {
        if (action === 'cancel') {
          Message.info("取消了分配权限")
        }
      })
    }
  }
}
</script>

<style scoped>
.borderHeader {
  border-top: 1px solid #eeeeee;
}

.borderBottom {
  border-bottom: 1px solid #eeeeee;
}

.vcenter {
  display: flex;
  align-items: center;
}

.el-tag {
  margin: 5px;
}
</style>
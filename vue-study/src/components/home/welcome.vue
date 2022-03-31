<template>
  <div id="welcome">
    <p>Welcome</p>
    <el-dialog title="欢迎" :visible.sync="visitorDialogVisible" width="50%">
      <span>您正以访客角色访问，该角色已被授予查看信息的全部权限，但没有其他权限</span>
      <span slot="footer" class="dialog-footer">
              <el-button @click="visitorDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="visitorDialogVisible = false">确 定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "welcome",
  created() {
    this.UserInfo = JSON.parse(window.localStorage.getItem("userInfo"))
    this.checkVisitor()
  },
  data() {
    return {
      UserInfo: {},
      visitorDialogVisible: false
    }
  },
  methods: {
    checkVisitor() {
      if (this.UserInfo.role === 'visitor')
        this.visitorDialogVisible = true
    }
  }
}
</script>

<style scoped>

</style>
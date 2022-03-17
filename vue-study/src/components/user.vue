<template>
  <div id="user">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片视图区域-->
    <el-card>
      <el-row gutter="20">
        <el-col span="7">
          <el-input placeholder="请输入内容">
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
        </el-col>
        <el-col span="4">
          <el-button type="primary">添加用户</el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "user",
  created() {
    this.getUserList()
  },
  data() {
    return {
      queryInfo: {
        query: '',
        currentPage: 1,
        pageSize: 2
      }
    }
  },
  methods: {
    getUserList() {
      this.$axios.getUerList().then((res) => {

      })
    }
  }

}
</script>

<style lang="less" scoped>

</style>
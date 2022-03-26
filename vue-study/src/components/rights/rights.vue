<template>
  <div id="rights">
    <!--面包屑导航栏-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item>权限列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片视图区域-->
    <el-card>
      <el-table :data="rightsList" border stripe>
        <el-table-column type="index"/>
        <el-table-column label="权限名称" prop="authName"/>
        <el-table-column label="路径" prop="paths"/>
        <el-table-column label="权限等级" prop="level">
          <template v-slot="scope">
            <el-tag v-if="scope.row.level === '0'">一级</el-tag>
            <el-tag type="success" v-else-if="scope.row.level === '1'">二级</el-tag>
            <el-tag type="warning" v-else>三级</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "rights",
  created() {
    this.getRights()
  },
  data() {
    return {
      rightsList: []//权限列表
    }
  },
  methods: {
    getRights() {//获取权限列表
      this.$axios.getRightsList().then((res) => {
        this.rightsList = res.data.data
        Message.success(res.data.msg)
      })
    }
  }
}
</script>

<style scoped>

</style>
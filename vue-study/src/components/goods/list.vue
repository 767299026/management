<template>
  <div id="list">
    <!--面包屑导航栏-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>商品列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片视图区域-->
    <el-card>
      <el-row>
        <el-col :span="8">
          <el-input placeholder="请输入内容" clearable v-model="queryInfo.query">
            <el-button slot="append" icon="el-icon-search" @click="getGoodsList()"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" style="margin-left: 15px" @click="goAddGoodsPage()">添加商品</el-button>
        </el-col>
      </el-row>
      <!--订单列表区域-->
      <el-table :data="currentPageData" border stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="商品名称" prop="goodsName"></el-table-column>
        <el-table-column label="商品价格" width="80px" prop="goodsPrice"></el-table-column>
        <el-table-column label="商品重量" width="70px" prop="goodsWeight"></el-table-column>
        <el-table-column label="创建时间" width="200px" prop="createTime"></el-table-column>
        <el-table-column label="修改时间" width="200px" prop="updateTime"></el-table-column>
        <el-table-column label="操作" width="160px">
          <template v-slot="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" title="编辑商品" @click="showEditGoodsDialog(scope.row.goodsId)"></el-button>
            <el-button type="danger" size="mini" icon="el-icon-delete"  title="删除商品" @click="confirmDeleteGoods(scope.row.goodsId)"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryInfo.currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="queryInfo.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalNums">
      </el-pagination>
    </el-card>

  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "list",
  created() {
    this.getGoodsList()
  },
  data() {
    return {
      queryInfo: {//查询当页商品列表的查询参数
        query: '',
        currentPage: 1,
        pageSize: 10
      },
      currentPageData: [],//当页商品列表数据
      totalNums: 0,//后台返回的商品总条数
      editGoodsDialogVisible: false,
    }
  },
  methods: {
    getGoodsList(){
      this.$axios.getGoods(this.queryInfo).then((res) => {
        this.currentPageData = res.data.data.records
        this.totalNums = res.data.data.total
      })
    },
    handleSizeChange(newSize) {//设置分页请求的页大小
      this.queryInfo.pageSize = newSize
      this.getGoodsList()
    },
    handleCurrentChange(newPage) {//设置分页请求的页标
      this.queryInfo.currentPage = newPage
      this.getGoodsList()
    },
    goAddGoodsPage() {
      this.$router.push('/goods/add')
    },
    showEditGoodsDialog(id) {
      this.$confirm("暂未实现该功能", '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.editAddressDialogVisible = false
      }).catch(action => {
        if (action === 'cancel') {
          this.editAddressDialogVisible = false
          Message.info("取消了操作")
        }
      })
    },
    confirmDeleteGoods(id) {
      this.$confirm("该操作不可逆，确认移除该商品？", '删除操作', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.deleteGoods(id).then((res) => {
          this.getGoodsList()
          Message.success(res.data.msg)
        })
      }).catch(action => {
        if (action === 'cancel') {
          Message.info("取消了移除操作")
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
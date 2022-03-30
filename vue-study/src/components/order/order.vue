<template>
  <div id="order">
    <!--面包屑导航栏-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>订单管理</el-breadcrumb-item>
      <el-breadcrumb-item>订单列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片视图区域-->
    <el-card>
      <el-row>
        <el-col :span="8">
          <el-input placeholder="请输入内容" clearable>
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <!--订单列表区域-->
      <el-table :data="currentPageData" border stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="订单编号" prop="orderNumber"></el-table-column>
        <el-table-column label="订单价格" prop="orderPrice"></el-table-column>
        <el-table-column label="是否付款">
          <template v-slot="scope">
            <el-tag type="success" v-if="scope.row.payStatus === '1'">已付款</el-tag>
            <el-tag type="danger" v-else>未付款</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="是否发货" prop="isSend"></el-table-column>
        <el-table-column label="下单时间" prop="createTime"></el-table-column>
        <el-table-column label="修改时间" prop="updateTime"></el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" title="修改地址" @click="showEditAddressDialog(scope.row)"></el-button>
            <el-button type="success" size="mini" icon="el-icon-location"  title="查看物流" @click="showProgress()"></el-button>
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

    <!--修改地址对话框-->
    <el-dialog title="修改地址" :visible.sync="editAddressDialogVisible" width="50%" @close="resetEditAddressForm()">
      <el-form :model="editForm" :rules="editFormRules" ref="editAddressFormRef" label-width="70px">
        <el-form-item label="省市区县" prop="area">
          <el-cascader :options="cityData" v-model="editForm.area" />
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="editForm.address"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editAddressDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editAddress()">确 定</el-button>
      </span>
    </el-dialog>

    <!--物流进度对话框-->
    <el-dialog title="查看物流" :visible.sync="progressDialogVisible" width="50%">
      <el-form ref="progressFormRef" label-width="70px">
        <el-timeline>
          <el-timeline-item v-for="(activity,index) in progressInfo" :key="index" :timestamp="activity.time">
            {{ activity.context }}
          </el-timeline-item>
        </el-timeline>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import {Message} from "element-ui";
import cityData from "../../utils/citydata";

export default {
  name: "order",
  created() {
    this.getOrderList()
  },
  data() {
    return {
      queryInfo: {//查询当页订单列表的查询参数
        query: '',
        currentPage: 1,
        pageSize: 10
      },
      currentPageData: [],//当页订单列表数据
      totalNums: 0,//后台返回的订单总条数
      editAddressDialogVisible: false,
      editForm: {
        area: [],
        address: ' '
      },
      editFormRules: {
        area: [
          {required: true, message: '请选择省市区县', trigger: 'blur'}
        ],
        address: [
          {required: true, message: '请填写详细地址', trigger: 'blur'}
        ]
      },
      cityData: cityData,
      progressDialogVisible: false,
      progressInfo: [],
    }
  },
  methods: {
    getOrderList() {//axios分页获取订单信息列表请求
      this.$axios.getOrderList(this.queryInfo).then((res) => {
        this.currentPageData = res.data.data.records
        this.totalNums = res.data.data.total
        Message.success(res.data.msg)
      })
    },
    handleSizeChange(newSize) {//设置分页请求的页大小
      this.queryInfo.pageSize = newSize
      this.getOrderList()
    },
    handleCurrentChange(newPage) {//设置分页请求的页标
      this.queryInfo.currentPage = newPage
      this.getOrderList()
    },
    resetEditAddressForm() {
      this.$refs.editAddressFormRef.resetFields()
    },
    showEditAddressDialog() {
      this.editAddressDialogVisible = true
    },
    editAddress() {
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
    showProgress() {
      this.progressInfo = [
        {
          "time": "2018-05-10 09:39:00",
          "ftime": "2018-05-10 09:39:00",
          "context": "已签收,感谢使用顺丰,期待再次为您服务",
          "location": ""
        },
        {
          "time": "2018-05-10 08:23:00",
          "ftime": "2018-05-10 08:23:00",
          "context": "[北京市]北京海淀育新小区营业点派件员 顺丰速运 95338正在为您派件",
          "location": ""
        },
        {
          "time": "2018-05-10 07:32:00",
          "ftime": "2018-05-10 07:32:00",
          "context": "快件到达 [北京海淀育新小区营业点]",
          "location": ""
        },
        {
          "time": "2018-05-10 02:03:00",
          "ftime": "2018-05-10 02:03:00",
          "context": "快件在[北京顺义集散中心]已装车,准备发往 [北京海淀育新小区营业点]",
          "location": ""
        },
        {
          "time": "2018-05-09 23:05:00",
          "ftime": "2018-05-09 23:05:00",
          "context": "快件到达 [北京顺义集散中心]",
          "location": ""
        },
        {
          "time": "2018-05-09 21:21:00",
          "ftime": "2018-05-09 21:21:00",
          "context": "快件在[北京宝胜营业点]已装车,准备发往 [北京顺义集散中心]",
          "location": ""
        },
        {
          "time": "2018-05-09 13:07:00",
          "ftime": "2018-05-09 13:07:00",
          "context": "顺丰速运 已收取快件",
          "location": ""
        },
        {
          "time": "2018-05-09 12:25:03",
          "ftime": "2018-05-09 12:25:03",
          "context": "卖家发货",
          "location": ""
        },
        {
          "time": "2018-05-09 12:22:24",
          "ftime": "2018-05-09 12:22:24",
          "context": "您的订单将由HLA（北京海淀区清河中街店）门店安排发货。",
          "location": ""
        },
        {
          "time": "2018-05-08 21:36:04",
          "ftime": "2018-05-08 21:36:04",
          "context": "商品已经下单",
          "location": ""
        }
      ]
      this.$confirm("数据库没有该数据，信息仅供展示", '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.progressDialogVisible = true
      }).catch(action => {
        if (action === 'cancel') {
          this.editAddressDialogVisible = false
          Message.info("取消了操作")
        }
      })
    }
  }
}
</script>

<style scoped>
.el-cascader {
  width: 100%;
}
</style>
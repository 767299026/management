<template>
  <div id="addGoods">
    <!--面包屑导航栏-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>添加商品</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片视图区域-->
    <el-card>
      <!--提示区域-->
      <el-alert title="添加商品信息" type="info" center show-icon :closable="false"></el-alert>
      <!--进度条-->
      <el-steps :space="200" :active="activeIndex - 0" finish-status="success" align-center>
        <el-step title="基本信息"></el-step>
        <el-step title="商品参数"></el-step>
        <el-step title="商品属性"></el-step>
        <el-step title="商品图片"></el-step>
        <el-step title="商品内容"></el-step>
        <el-step title="完成"></el-step>
      </el-steps>
      <el-form :model="addForm" :rules="addFormRules" ref="addGoodsFormRef" label-width="100px" label-position="top">
        <!--Tab栏区域-->
        <el-tabs v-model="activeIndex" :tab-position="'left'" :before-leave="beforeTabLeave" @tab-click="tabClicked()">
          <el-tab-pane label="基本信息" name="0">
            <el-form-item label="商品名称" prop="goodsName">
              <el-input v-model="addForm.goodsName" />
            </el-form-item>
            <el-form-item label="商品价格" prop="goodsPrice">
              <el-input v-model="addForm.goodsPrice" type="number"/>
            </el-form-item>
            <el-form-item label="商品重量" prop="goodsWeight">
              <el-input v-model="addForm.goodsWeight" />
            </el-form-item>
            <el-form-item label="商品数量" prop="goodsNumber">
              <el-input v-model="addForm.goodsNumber" />
            </el-form-item>
            <!--选择商品分类的级联选择框-->
            <el-form-item label="商品分类">
              <el-cascader v-model="goodsCategory" expand-trigger="hover" :options="categoryList"
                           :props="cascadeProps" clearable @change="cateChange()">
              </el-cascader>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="商品参数" name="1">
            <!--渲染表单的item项-->
            <el-form-item v-for="item in manyTableData" :label="item.attrName" :key="item.attrId">
              <el-checkbox-group v-model="item.attrVals">
                <el-checkbox size="mini" v-for="(name,index) in item.attrVals" :label="name" :key="index" border></el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="商品属性" name="2">
            <el-form-item v-for="item in onlyTableData" :label="item.attrName" :key="item.attrId">
              <el-checkbox-group v-model="item.attrVals">
                <el-input v-model="item.attrVals"></el-input>
              </el-checkbox-group>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="商品图片" name="3">
            <el-upload class="avatar-uploader" :action="uploadUrl"
                :show-file-list="false" :on-success="handleAvatarSuccess"
                       :before-upload="beforeAvatarUpload" :headers="header">
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-tab-pane>
          <el-tab-pane label="商品内容" name="4">
            <quill-editor v-model="addForm.goodsIntroduce"></quill-editor>
            <el-button type="primary" style="margin-top: 10px" @click="addGoods()">添加商品</el-button>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </el-card>

  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "addGoods",
  created() {
    this.getCategoryList()
  },
  data() {
    return {
      categoryList: [],//商品分类列表
      cascadeProps: {//级联选择器配置对象
        value: 'categoryId',
        label: 'categoryName',
        children: 'children'
      },
      activeIndex: '0',//激活的索引
      goodsCategory: [],//商品的分类
      addForm: {//添加商品表单
        goodsName: '',
        goodsPrice: 0,
        goodsWeight: 0,
        goodsNumber: 0,
        goodsBigLogo: '',
        catOneId: 0,
        catTwoId: 0,
        catThreeId: 0,
        goodsIntroduce: ''
      },
      addFormRules: {//添加商品表单验证规则
        goodsName: [
          {required: true, message: '请输入商品名称', trigger: 'blur'}
        ],
        goodsPrice: [
          {required: true, message: '请输入商品价格', trigger: 'blur'}
        ],
        goodsWeight: [
          {required: true, message: '请输入商品重量', trigger: 'blur'}
        ],
        goodsNumber: [
          {required: true, message: '请输入商品数量', trigger: 'blur'}
        ]
      },
      manyTableData: [],//动态参数数据
      onlyTableData: [],//静态属性数据
      uploadUrl: 'http://localhost:8484/vue/goods/upload',
      imageUrl: '',
      header: {
        Authorization: window.localStorage.getItem("token")
      }
    }
  },
  methods: {
    getCategoryList() {//axios获取商品分类请求
      this.$axios.Categories().then((res) => {
        this.categoryList = res.data.data
      })
    },
    cateChange() {//级联选择器选择商品分类
      if (this.goodsCategory.length !== 3) {
        this.goodsCategory = []
        Message.error("请选择商品分类")
      }
    },
    beforeTabLeave(activeName,oldActiveName) {//选择下级tab标签的判断
      if (oldActiveName === '0' && this.goodsCategory.length !== 3) {
        Message.error("请选择商品分类")
        return false
      }
    },
    tabClicked() {
      if (this.activeIndex === '1') {
        this.$axios.getCategoryAttributes(this.cateId,'many').then((res) => {
          const pageData = res.data.data
          pageData.forEach(item => {
            item.attrVals = item.attrVals ?
                item.attrVals.split(' ') : []
          })
          this.manyTableData = pageData
        })
      } else if (this.activeIndex === '2') {
        this.$axios.getCategoryAttributes(this.cateId,'only').then((res) => {
          const pageData = res.data.data
          pageData.forEach(item => {
            item.attrVals = item.attrVals ?
                item.attrVals.split(' ') : []
          })
          this.onlyTableData = pageData
        })
      }
    },
    handleAvatarSuccess(res,file) {
      if (res.code === 200 && res.success === true) {
        this.imageUrl = URL.createObjectURL(file.raw)
        this.addForm.goodsBigLogo = res.data
        Message.success(res.msg)
      } else
        Message.error(res.msg)
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isLt2M;
    },
    addGoods() {
      this.$refs.addGoodsFormRef.validate(valid => {
        if (!valid) return Message.error("商品信息未完善")
        this.addForm.catOneId = this.goodsCategory[0]
        this.addForm.catTwoId = this.goodsCategory[1]
        this.addForm.catThreeId = this.goodsCategory[2]
        this.$axios.addGoods(this.addForm).then((res) => {
          this.$router.push('/goods')
          Message.success(res.data.msg)
        })
      })
    }
  },
  computed: {
    cateId() {
      if(this.goodsCategory.length === 3)
        return this.goodsCategory[2]
      return null
    }
  }
}
</script>

<style scoped>
.el-checkbox {
  margin: 0 10px 0 0 !important;
}
</style>
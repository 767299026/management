<template>
  <div id="cate">
    <!--面包屑导航栏-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>商品分类</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片视图区域-->
    <el-card>
      <el-row>
        <el-col>
          <el-button type="primary" @click="showAddCategoryDialogVisible()">添加分类</el-button>
        </el-col>
      </el-row>
      <!--树形表格区域-->
      <tree-table :data="currentCateList" :columns="columns"
                  :selection-type="false" :expand-type="false"
                  :show-index="true" index-text=" " border class="tree">
        <!--是否有效-->
        <template v-slot:status="scope">
          <i class="el-icon-success" style="color: lightgreen" v-if="scope.row.categoryStatus === false"/>
          <i class="el-icon-error" style="color: lightcoral" v-else></i>
        </template>
        <!--分类等级-->
        <template v-slot:order="scope">
          <el-tag size="mini" v-if="scope.row.categoryLevel === 0">一级</el-tag>
          <el-tag size="mini" type="success" v-else-if="scope.row.categoryLevel === 1">二级</el-tag>
          <el-tag size="mini" type="warning" v-else="scope.row.categoryLevel === 2">三级</el-tag>
        </template>
        <!--操作-->
        <template v-slot:option="scope">
          <el-button size="mini" type="primary" icon="el-icon-edit"
                     @click="showEditCategoryDialogVisible(scope.row.categoryId)">编辑
          </el-button>
          <el-button size="mini" type="danger" icon="el-icon-delete"
                     @click="confirmDeleteCategory(scope.row.categoryId)">删除
          </el-button>
        </template>
      </tree-table>
      <!--分页-->
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryInfo.currentPage"
          :page-sizes="[5, 10 ,15]"
          :page-size="queryInfo.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalNums">
      </el-pagination>
    </el-card>

    <!--添加分类对话框-->
    <el-dialog title="添加分类" :visible.sync="addCategoryDialogVisible" width="50%" @close="resetAddCategoryForm()">
      <el-form :model="addForm" :rules="addFormRules" ref="addCategoryFormRef" label-width="70px">
        <el-form-item label="分类名称" label-width="80px" prop="categoryName">
          <el-input v-model="addForm.categoryName"/>
        </el-form-item>
        <el-form-item label="分类等级" label-width="80px">
          <el-cascader v-model="selectedKeys" expand-trigger="hover" :options="parentCategories"
                       :props="cascadeProps" @change="addCateChanged()" clearable change-on-select>
          </el-cascader>
        </el-form-item>
        <el-form-item label="是否有效" label-width="80px">
          <el-switch v-model="addForm.categoryStatus" :active-value="false" :inactive-value="true"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addCategoryDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addCategory()">确 定</el-button>
      </span>
    </el-dialog>

    <!--编辑分类对话框-->
    <el-dialog title="编辑分类" :visible.sync="editCategoryDialogVisible" width="50%" @close="resetEditCategoryForm()">
      <el-form :model="editForm" :rules="editFormRules" ref="editCategoryFormRef" label-width="70px">
        <el-form-item label="categoryId" label-width="85px">
          <el-input v-model="editForm.categoryId" disabled/>
        </el-form-item>
        <el-form-item label="分类名称" label-width="80px" prop="categoryName">
          <el-input v-model="editForm.categoryName"/>
        </el-form-item>
        <el-form-item label="分类等级" label-width="80px">
          <el-cascader v-model="selectedKeys" expand-trigger="hover" :options="parentCategories"
                       :props="cascadeProps" @change="editCateChanged()" clearable change-on-select>
          </el-cascader>
        </el-form-item>
        <el-form-item label="是否有效" label-width="80px">
          <el-switch v-model="editForm.categoryStatus" :active-value="false" :inactive-value="true"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editCategoryDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editCategory()">确 定</el-button>
      </span>
    </el-dialog>


  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "cate",
  created() {
    this.getGoodsCategories()
  },
  data() {
    return {
      queryInfo: {//分页查询参数
        type: 3,
        currentPage: 1,
        pageSize: 5
      },
      currentCateList: [],//当前页分类列表
      totalNums: 0,//分类总条数
      columns: [//树形表
        {
          label: '分类名称',
          prop: 'categoryName',
        },
        {
          label: '是否有效',
          type: 'template',
          template: 'status'
        },
        {
          label: '排序',
          type: 'template',
          template: 'order'
        },
        {
          label: '操作',
          type: 'template',
          template: 'option'
        }
      ],
      addCategoryDialogVisible: false,//添加分类对话框
      parentCategories: [],//父级分类列表
      cascadeProps: {//级联选择器配置对象
        value: 'categoryId',
        label: 'categoryName',
        children: 'children'
      },
      selectedKeys: [],//级联选择器选中的节点
      addForm: {//添加分类表单
        categoryName: '',
        categoryLevel: 0,
        parentId: 0,
        categoryStatus: false
      },
      addFormRules: {//添加分类表单验证规则
        categoryName: [
          {required: true, message: '请输入分类名称', trigger: 'blur'},
          {max: 12, message: "分类名称超限", trigger: "blur"}
        ]
      },
      editCategoryDialogVisible: false,//编辑分类对话框
      editForm: {//编辑分类表单
        categoryId: 0,
        categoryName: '',
        categoryLevel: 0,
        parentId: [],
        categoryStatus: false
      },
      editFormRules: {//编辑分类表单验证规则
        categoryName: [
          {required: true, message: '请输入分类名称', trigger: 'blur'},
          {max: 12, message: "分类名称超限", trigger: "blur"}
        ]
      }
    }
  },
  methods: {
    getGoodsCategories() {//axios分页获取商品分类列表请求
      this.$axios.getCategories(this.queryInfo).then((res) => {
        this.currentCateList = res.data.data.records
        this.totalNums = res.data.data.total
        Message.success(res.data.msg)
      })
    },
    handleSizeChange(newSize) {//设置分页请求的页大小
      this.queryInfo.pageSize = newSize
      this.getGoodsCategories()
    },
    handleCurrentChange(newPage) {//设置分页请求的页标
      this.queryInfo.currentPage = newPage
      this.getGoodsCategories()
    },
    resetAddCategoryForm() {//重设添加分类表单
      this.$refs.addCategoryFormRef.resetFields()
      this.selectedKeys = []
    },
    showAddCategoryDialogVisible() {//展示添加分类对话框
      this.$axios.getParentCategories(2).then((res) => {
        this.parentCategories = res.data.data
      })
      this.addCategoryDialogVisible = true
    },
    addCateChanged() {//监听添加分类对话框的分类等级
      if (this.selectedKeys.length > 0) {
        this.addForm.parentId = this.selectedKeys[this.selectedKeys.length - 1]
        this.addForm.categoryLevel = this.selectedKeys.length
      } else {
        this.addForm.parentId = 0
        this.addForm.categoryLevel = 0
      }
    },
    addCategory() {//axios添加分类请求
      this.$confirm("如果要添加一级分类，请不要选择父级分类", '添加分类', {
        confirmButtonText: '确认添加',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$refs.addCategoryFormRef.validate(valid => {
          if (!valid) return
          this.$axios.addGoodsCategory(this.addForm).then((res) => {
            this.addCategoryDialogVisible = false
            this.getGoodsCategories()
            Message.success(res.data.msg)
          })
        })
      }).catch(action => {
        if (action === 'cancel') {
          Message.info("取消了添加操作")
        }
      })
    },
    resetEditCategoryForm() {//重设编辑分类表单
      if (this.$refs.editCategoryFormRef !== undefined)
        this.$refs.editCategoryFormRef.resetFields()
      this.selectedKeys = []
    },
    showEditCategoryDialogVisible(id) {//展示编辑分类对话框
      this.$axios.getParentCategories(2).then((res) => {
        this.parentCategories = res.data.data
      })
      this.$axios.getCategoryInfo(id).then((res) => {
        this.editForm = res.data.data
        this.selectedKeys = this.editForm.parentId
        this.editCategoryDialogVisible = true
        Message.success(res.data.msg)
      })
    },
    editCateChanged() {//监听编辑分类对话框的分类等级
      if (this.selectedKeys.length > 0) {
        this.editForm.parentId = this.selectedKeys
        this.editForm.categoryLevel = this.selectedKeys.length
      } else {
        this.editForm.parentId = [0]
        this.editForm.categoryLevel = 0
      }
    },
    editCategory() {//axios编辑分类请求
      this.$refs.editCategoryFormRef.validate(valid => {
        if (!valid) return
        this.$axios.editGoodsCategory(this.editForm).then((res) => {
          this.editCategoryDialogVisible = false
          this.getGoodsCategories()
          Message.success(res.data.msg)
        })
      })
    },
    confirmDeleteCategory(id) {//确认删除分类
      this.$confirm("此操作会删除包含所有子分类的所有分类，请慎重选择", '删除分类', {
        confirmButtonText: '确认删除',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.deleteGoodsCategory(id).then((res) => {
          this.getGoodsCategories()
          Message.success(res.data.msg)
        })
      }).catch(action => {
        if (action === 'cancel') {
          Message.info("取消了删除操作")
        }
      })
    },
  }
}
</script>

<style scoped>
.tree {
  margin-top: 20px;
}

.el-cascader {
  width: 100%;
}
</style>
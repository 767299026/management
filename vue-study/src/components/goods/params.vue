<template>
  <div id="params">
    <!--面包屑导航栏-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>参数管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片视图区域-->
    <el-card>
      <!--警告区域-->
      <el-alert title="注意：只允许为第三级分类设置相关参数！" type="warning" :closable="false"/>
      <!--选择商品分类区域-->
      <el-row class="cate_opt">
        <el-col>
          <span>选择商品分类：</span>
          <!--选择商品分类的级联选择框-->
          <el-cascader v-model="selectedKeys" expand-trigger="hover" :options="categoryList"
                       :props="cascadeProps" clearable @change="cateChange()">
          </el-cascader>
        </el-col>
      </el-row>
      <!--Tab页签区域-->
      <el-tabs v-model="activeName" @tab-click="handleTabClick()">
        <el-tab-pane label="动态参数" name="many">
          <el-button type="primary" size="mini" :disabled="isDisabled" @click="addAttributeDialogVisible = true">添加参数
          </el-button>
          <!--动态参数表格-->
          <el-table :data="manyTableData" border stripe>
            <el-table-column type="expand">
              <template v-slot="scope">
                <!--循环渲染Tag标签-->
                <el-tag v-for="(item,index) in scope.row.attrVals" :key="index" closable
                        @close="handleClose(index,scope.row)">
                  {{ item }}
                </el-tag>
                <!--添加Tag-->
                <el-input class="input-new-tag" v-if="scope.row.inputVisible" v-model="scope.row.inputValue"
                          ref="saveTagInput" size="small" @keyup.enter.native="$event.target.blur()"
                          @blur="handleInputConfirm(scope.row)"/>
                <!--添加按钮-->
                <el-button v-else class="button-new-tag" size="small" @click="showInput(scope.row)">New Tag</el-button>
              </template>
            </el-table-column>
            <el-table-column type="index"/>
            <el-table-column label="参数名称" prop="attrName"/>
            <el-table-column label="操作">
              <template v-slot="scope">
                <el-button size="mini" type="primary" icon="el-icon-edit"
                           @click="showEditAttributeDialogVisible(scope.row.attrId)">编辑
                </el-button>
                <el-button size="mini" type="danger" icon="el-icon-delete"
                           @click="confirmDeleteAttribute(scope.row.attrId)">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="静态属性" name="only">
          <el-button type="primary" size="mini" :disabled="isDisabled" @click="addAttributeDialogVisible = true">添加属性
          </el-button>
          <!--静态参数表格-->
          <el-table :data="onlyTableData" border stripe>
            <el-table-column type="expand">
              <template v-slot="scope">
                <!--循环渲染Tag标签-->
                <el-tag v-for="(item,index) in scope.row.attrVals" :key="index" closable
                        @close="handleClose(index,scope.row)">
                  {{ item }}
                </el-tag>
                <!--添加Tag-->
                <el-input class="input-new-tag" v-if="scope.row.inputVisible" v-model="scope.row.inputValue"
                          ref="saveTagInput" size="small" @keyup.enter.native="$event.target.blur()"
                          @blur="handleInputConfirm(scope.row)"/>
                <!--添加按钮-->
                <el-button v-else class="button-new-tag" size="small" @click="showInput(scope.row)">New Tag</el-button>
              </template>
            </el-table-column>
            <el-table-column type="index"/>
            <el-table-column label="属性名称" prop="attrName"></el-table-column>
            <el-table-column label="操作">
              <template v-slot="scope">
                <el-button size="mini" type="primary" icon="el-icon-edit"
                           @click="showEditAttributeDialogVisible(scope.row.attrId)">编辑
                </el-button>
                <el-button size="mini" type="danger" icon="el-icon-delete"
                           @click="confirmDeleteAttribute(scope.row.attrId)">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!--添加参数对话框-->
    <el-dialog :title="'添加'+titleText" :visible.sync="addAttributeDialogVisible" width="50%"
               @close="resetAddAttributeForm()">
      <el-form :model="addForm" :rules="addFormRules" ref="addAttributeFormRef" label-width="70px">
        <el-form-item :label="attributeName+'名称'" label-width="80px" prop="attrName">
          <el-input v-model="addForm.attrName"/>
        </el-form-item>
        <el-form-item label="录入方式" label-width="80px">
          <el-switch v-model="addForm.attrWrite" active-text="列表选择" inactive-text="手工录入"
                     :active-value="'list'" :inactive-value="'manual'" style="margin-left: 20px"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addCategoryDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addAttribute()">确 定</el-button>
      </span>
    </el-dialog>

    <!--编辑参数对话框-->
    <el-dialog :title="'修改'+titleText" :visible.sync="editAttributeDialogVisible" width="50%"
               @close="resetEditAttributeForm()">
      <el-form :model="editForm" :rules="editFormRules" ref="editAttributeFormRef" label-width="70px">
        <el-form-item label="attrId" label-width="80px">
          <el-input v-model="editForm.attrId" disabled/>
        </el-form-item>
        <el-form-item :label="attributeName+'名称'" label-width="80px" prop="attrName">
          <el-input v-model="editForm.attrName"/>
        </el-form-item>
        <el-form-item label="录入方式" label-width="80px">
          <el-switch v-model="editForm.attrWrite" active-text="列表选择" inactive-text="手工录入"
                     :active-value="'list'" :inactive-value="'manual'" style="margin-left: 20px"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editAttributeDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editAttribute()">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "params",
  created() {
    this.getCategories()
  },
  data() {
    return {
      categoryList: [],//分类列表
      cascadeProps: {//级联选择器配置对象
        value: 'categoryId',
        label: 'categoryName',
        children: 'children'
      },
      selectedKeys: [],//级联选择器选中的节点
      activeName: 'many',//tab标签默认激活名称
      manyTableData: [],//动态参数表格数据
      onlyTableData: [],//静态参数表格数据
      addAttributeDialogVisible: false,
      addForm: {//添加参数表单
        attrName: '',
        catId: 0,
        attrSel: '',
        attrWrite: '',
      },
      addFormRules: {//添加参数表单验证规则
        attrName: [
          {required: true, message: '请输入名称', trigger: 'blur'},
          {max: 12, message: "名称超限", trigger: "blur"}
        ]
      },
      editAttributeDialogVisible: false,//修改参数对话框
      editForm: {//修改参数表单
        attrId: 0,
        attrName: '',
        catId: 0,
        attrSel: '',
        attrWrite: '',
      },
      editTagParams: {//修改参数Tag
        attrId: 0,
        attrVals: ''
      },
      editFormRules: {//修改参数表单验证规则
        attrName: [
          {required: true, message: '请输入名称', trigger: 'blur'},
          {max: 12, message: "名称超限", trigger: "blur"}
        ]
      }
    }
  },
  methods: {
    getCategories() {//获取分类列表
      this.$axios.Categories().then((res) => {
        this.categoryList = res.data.data
      })
    },
    cateChange() {//根据所选分类的Id和当前所处面板发起请求
      this.getCategoryAttributes()
    },
    handleTabClick() {//tab页签变化时发起请求
      this.getCategoryAttributes()
    },
    getCategoryAttributes() {//axios获取参数请求
      if (this.selectedKeys.length !== 3) {
        this.manyTableData = []
        this.onlyTableData = []
        Message.error("请选择商品分类")
        return
      }
      this.$axios.getCategoryAttributes(this.categoryId, this.activeName).then((res) => {
        const pageData = res.data.data
        pageData.forEach(item => {
          item.attrVals = item.attrVals ?
              item.attrVals.split(' ') : []
          item.inputVisible = false
          item.inputValue = ' '
        })
        if (this.activeName === 'many')
          this.manyTableData = pageData
        else
          this.onlyTableData = pageData
      })
    },
    resetAddAttributeForm() {//重设添加参数表单
      this.$refs.addAttributeFormRef.resetFields()
    },
    addAttribute() {//axios添加参数请求
      this.$refs.addAttributeFormRef.validate(valid => {
        if (!valid) return
        this.addForm.catId = this.categoryId
        this.addForm.attrSel = this.activeName
        this.$axios.addAttributes(this.addForm).then((res) => {
          this.addAttributeDialogVisible = false
          this.getCategoryAttributes()
          Message.success(res.data.msg)
        })
      })
    },
    resetEditAttributeForm() {//重设添加参数表单
      if (this.$refs.editAttributeFormRef !== undefined)
        this.$refs.editAttributeFormRef.resetFields()
    },
    showEditAttributeDialogVisible(id) {//展示编辑参数对话框
      this.$axios.getAttributes(id).then((res) => {
        this.editForm = res.data.data
        this.editAttributeDialogVisible = true
        Message.success(res.data.msg)
      })
    },
    editAttribute() {//axios编辑参数请求
      this.$refs.addAttributeFormRef.validate(valid => {
        if (!valid) return
        this.$axios.editAttributes(this.editForm).then((res) => {
          this.editAttributeDialogVisible = false
          this.getCategoryAttributes()
          Message.success(res.data.msg)
        })
      })
    },
    confirmDeleteAttribute(id) {//axios删除参数请求
      this.$confirm("确认删除该分类的这条参数？", '删除参数', {
        confirmButtonText: '确认删除',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.deleteAttributes(id).then((res) => {
          this.getCategoryAttributes()
          Message.success(res.data.msg)
        })
      }).catch(action => {
        if (action === 'cancel') {
          Message.info("取消了删除操作")
        }
      })
    },
    handleInputConfirm(item) {//axios添加参数Tag请求
      if (item.inputValue.trim().length === 0) {
        Message.error("输入了空信息")
        item.inputValue = ''
        item.inputVisible = false
      } else {
        item.attrVals.push(item.inputValue.trim())
        item.inputValue = ''
        item.inputVisible = false
        this.editTagParams.attrId = item.attrId
        this.editTagParams.attrVals = item.attrVals.join(' ')
        this.editAttributeTag()
      }
    },
    handleClose(index, item) {//axios删除参数Tag请求
      item.attrVals.splice(index, 1)
      this.editTagParams.attrId = item.attrId
      this.editTagParams.attrVals = item.attrVals.join(' ')
      this.editAttributeTag()
    },
    editAttributeTag() {
      this.$axios.editAttributesTag(this.editTagParams).then((res) => {
        Message.success(res.data.msg)
        this.editTagParams.attrId = 0
        this.editTagParams.attrVals = ' '
      })
    },
    showInput(item) {//打开Tag的输入文本框
      item.inputVisible = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    }
  },
  computed: {
    isDisabled() {//如果按钮需要被禁用，返回true
      return (this.selectedKeys.length !== 3)
    },
    categoryId() {//返回级联选择器的分类Id
      if (this.selectedKeys.length === 3)
        return this.selectedKeys[2]
      return null
    },
    titleText() {//返回对话框的标题
      if (this.activeName === 'many')
        return '动态参数'
      return '静态属性'
    },
    attributeName() {//返回对话框的名称
      if (this.activeName === 'many')
        return '参数'
      return '属性'
    },
  }
}
</script>

<style scoped>
.cate_opt {
  margin: 15px;
}

.el-tag {
  margin-left: 10px;
  margin-right: 10px;
}

.input-new-tag {
  margin: 10px;
  width: 150px;
}

.button-new-tag {
  margin-left: 10px;
  margin-right: 10px;
  margin-top: 10px;
}
</style>
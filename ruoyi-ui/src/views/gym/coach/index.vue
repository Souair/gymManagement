<template>
  <div class="app-container">

    <el-card class="search-card" shadow="hover">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="70px">
        <el-row :gutter="10">
          <el-col :span="5">
            <el-form-item label="搜索教练" prop="name" style="margin-bottom: 0; width: 100%">
              <el-input
                v-model="queryParams.name"
                placeholder="姓名 / 手机 / 编号"
                clearable
                prefix-icon="el-icon-search"
                @keyup.enter.native="handleQuery"
                style="width: 100%"
                class="custom-round-input"
              />
            </el-form-item>
          </el-col>

          <el-col :span="4">
            <el-form-item label="专业领域" prop="specialty" style="margin-bottom: 0; width: 100%">
              <el-select v-model="queryParams.specialty" placeholder="请选择" clearable style="width: 100%" @change="handleQuery">
                <el-option
                  v-for="item in specialtyOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="4">
            <el-form-item label="在职状态" prop="status" style="margin-bottom: 0; width: 100%">
              <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 100%" @change="handleQuery">
                <el-option label="在职" value="1"></el-option>
                <el-option label="离职" value="0"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label="性别" prop="gender" style="margin-bottom: 0">
              <el-radio-group v-model="queryParams.gender" size="small" @input="handleQuery">
                <el-radio-button :label="null">全部</el-radio-button>
                <el-radio-button label="0"><i class="el-icon-male"></i> 男</el-radio-button>
                <el-radio-button label="1"><i class="el-icon-female"></i> 女</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="5" style="text-align: right">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="table-card" shadow="hover">
      <div slot="header" class="clearfix">
        <span class="card-title"><i class="el-icon-s-custom"></i> 教练列表</span>
        <div class="right-actions">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['gym:coach:add']">新增教练</el-button>
          <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['gym:coach:edit']">修改</el-button>
          <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['gym:coach:remove']">删除</el-button>
          <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['gym:coach:export']">导出</el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="coachList"
        @selection-change="handleSelectionChange"
        :header-cell-style="{background:'#f8f9fa', color:'#606266', fontWeight:'bold'}"
        style="width: 100%"
      >
        <el-table-column type="selection" width="50" align="center" />

        <el-table-column label="编号" align="left" prop="coachNo" width="120">
          <template slot-scope="scope">
            <div class="card-no-tag">
              {{ scope.row.coachNo }}
            </div>
          </template>
        </el-table-column>

        <el-table-column label="教练信息" align="left" width="220">
          <template slot-scope="scope">
            <div class="user-info-wrapper">
              <el-avatar
                shape="square"
                :size="45"
                :src="scope.row.avatar ? (baseUrl + scope.row.avatar) : ''"
                icon="el-icon-user-solid"
                fit="cover"
                class="info-avatar">
              </el-avatar>

              <div class="user-info-text">
                <div class="user-name">
                  {{ scope.row.name }}
                  <i v-if="scope.row.gender === '0' || scope.row.gender === '男'" class="el-icon-male" style="color: #409EFF; margin-left:5px"></i>
                  <i v-if="scope.row.gender === '1' || scope.row.gender === '女'" class="el-icon-female" style="color: #F56C6C; margin-left:5px"></i>
                </div>
                <div class="user-phone">{{ scope.row.phone }}</div>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="年龄" align="center" prop="age" width="60" />

        <el-table-column label="身体数据" align="center" width="150">
          <template slot-scope="scope">
            <div class="body-data">
              <el-tag size="mini" type="info" effect="plain" v-if="scope.row.height">{{ scope.row.height }}cm</el-tag>
              <el-tag size="mini" type="info" effect="plain" v-if="scope.row.weight">{{ scope.row.weight }}kg</el-tag>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="专业领域" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-tag size="small" effect="light" type="success" v-if="scope.row.specialty">{{ scope.row.specialty }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="入职时间" align="center" width="120">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.entryDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>

        <el-table-column label="状态" align="center" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '1' ? 'primary' : 'danger'" effect="dark" size="mini">
              {{ scope.row.status === '1' ? '在职' : '离职' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" style="color: #409EFF">编辑</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" style="color: #F56C6C">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body custom-class="custom-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">

        <div class="form-section-title">👤 基本信息</div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="教练编号" prop="coachNo">
              <el-input v-model="form.coachNo" placeholder="自动生成/输入" :disabled="form.coachId != null" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio label="0">男</el-radio>
                <el-radio label="1">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="照片上传" prop="avatar">
              <image-upload v-model="form.avatar" :limit="1"/>
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-section-title">💪 专业与身体数据</div>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="身高(cm)" prop="height">
              <el-input-number v-model="form.height" :min="100" :max="250" controls-position="right" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="体重(kg)" prop="weight">
              <el-input-number v-model="form.weight" :min="40" :max="150" controls-position="right" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="form.age" :min="18" :max="60" controls-position="right" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="专业领域" prop="specialty">
              <el-select v-model="form.specialty" placeholder="请选择擅长领域" style="width: 100%" allow-create filterable default-first-option>
                <el-option v-for="item in specialtyOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <<div class="form-section-title">📅 入职状态</div>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="当前状态" prop="status">
              <el-radio-group v-model="form.status" size="small">
                <el-radio-button label="1">在职</el-radio-button>
                <el-radio-button label="0">离职</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="入职日期" prop="entryDate">
              <el-date-picker
                clearable
                v-model="form.entryDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="form.status === '0'">
            <el-form-item label="离职日期" prop="resignDate">
              <el-date-picker
                clearable
                v-model="form.resignDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注信息" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCoach, getCoach, delCoach, addCoach, updateCoach } from "@/api/gym/coach"
import ImageUpload from "@/components/ImageUpload"

export default {
  name: "Coach",
  components: {
    ImageUpload
  },
  data() {
    return {
      baseUrl: process.env.VUE_APP_BASE_API,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 教练信息表格数据
      coachList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null, // 复用name作为模糊搜索
        status: null,
        specialty: null,
        gender: null,
      },
      // 专业领域选项 (如果你有字典接口，可以在created里加载)
      specialtyOptions: [
        { label: '增肌', value: '增肌' },
        { label: '减脂', value: '减脂' },
        { label: '塑形', value: '塑形' },
        { label: '瑜伽', value: '瑜伽' },
        { label: '普拉提', value: '普拉提' },
        { label: '康复训练', value: '康复训练' },
        { label: '拳击', value: '拳击' }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "手机不能为空", trigger: "blur" },
          { pattern: /^1[3-9]\d{9}$/, message: "手机号格式不正确", trigger: "blur" }
        ],
        coachNo: [
          { required: true, message: "教练编号不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询教练信息列表 */
    getList() {
      this.loading = true
      listCoach(this.queryParams).then(response => {
        this.coachList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        coachId: null,
        coachNo: 'C' + Math.floor(1000 + Math.random() * 9000), // 模拟自动生成
        name: null,
        avatar: null,
        gender: "0",
        age: undefined,
        phone: null,
        height: undefined,
        weight: undefined,
        specialty: null,
        entryDate: new Date().toISOString().slice(0, 10),
        resignDate: null,
        status: "1",
        delFlag: null,
        remark: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.coachId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "✨ 新增教练"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const coachId = row.coachId || this.ids
      getCoach(coachId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "📝 修改教练信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.coachId != null) {
            updateCoach(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addCoach(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const coachIds = row.coachId || this.ids
      this.$modal.confirm('是否确认删除教练信息编号为"' + coachIds + '"的数据项？').then(function() {
        return delCoach(coachIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('gym/coach/export', {
        ...this.queryParams
      }, `coach_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped lang="scss">
.app-container {
  background-color: #f5f7fa;
  min-height: 100vh;
  padding: 20px;
}

/* 卡片通用样式 */
.search-card, .table-card {
  border-radius: 8px;
  border: none;
  margin-bottom: 20px;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.right-actions {
  float: right;
}

/* 搜索框美化：只让顶部的搜索框变圆 */
::v-deep .custom-round-input .el-input__inner {
  border-radius: 20px;
}

/* 表格样式美化 */
.user-info-wrapper {
  display: flex;
  align-items: center;
}
.info-avatar {
  margin-right: 10px;
  background-color: #f0f2f5;
  color: #909399;
}
.user-info-text {
  display: flex;
  flex-direction: column;
}
.user-name {
  font-weight: bold;
  font-size: 14px;
  color: #303133;
}
.user-phone {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.card-no-tag {
  background-color: #f0f9eb;
  color: #67c23a;
  padding: 4px 8px;
  border-radius: 4px;
  font-family: monospace;
  font-weight: bold;
  display: inline-block;
  border: 1px solid #e1f3d8;
}

.body-data {
  display: flex;
  gap: 5px;
  justify-content: center;
}

/* 弹窗样式 */
.form-section-title {
  font-size: 15px;
  font-weight: bold;
  color: #303133;
  margin: 10px 0 20px 0;
  padding-left: 10px;
  border-left: 4px solid #409EFF;
}
/* 强制让头像里的图片撑满 */
::v-deep .info-avatar > img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>

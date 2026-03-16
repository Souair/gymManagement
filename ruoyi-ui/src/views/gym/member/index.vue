<template>
  <div class="app-container">

    <el-card class="search-card" shadow="hover">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="70px">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="搜索会员" prop="keyword" style="margin-bottom: 0; width: 100%">
              <el-input
                v-model="queryParams.name"
                placeholder="输入姓名 / 手机号 / 卡号"
                clearable
                prefix-icon="el-icon-search"
                @keyup.enter.native="handleQuery"
                style="width: 100%"
                class="custom-round-input"
              />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="性别" prop="gender" style="margin-bottom: 0">
              <el-radio-group v-model="queryParams.gender" size="small" @input="handleQuery">
                <el-radio-button :label="null">全部</el-radio-button>
                <el-radio-button label="男"><i class="el-icon-male"></i> 男</el-radio-button>
                <el-radio-button label="女"><i class="el-icon-female"></i> 女</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8" style="text-align: right">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="table-card" shadow="hover">
      <div slot="header" class="clearfix">
        <span class="card-title"><i class="el-icon-s-custom"></i> 会员列表</span>
        <div class="right-actions">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增会员</el-button>
          <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
          <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
          <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="memberList"
        @selection-change="handleSelectionChange"
        :header-cell-style="{background:'#f8f9fa', color:'#606266', fontWeight:'bold'}"
        style="width: 100%"
      >
        <el-table-column type="selection" width="50" align="center" />

        <el-table-column label="会员卡号" align="left" prop="cardNo" width="130">
          <template slot-scope="scope">
            <div class="card-no-tag">
              <i class="el-icon-bank-card"></i> {{ scope.row.cardNo }}
            </div>
          </template>
        </el-table-column>

        <el-table-column label="会员信息" align="left" width="160">
          <template slot-scope="scope">
            <div class="user-info-cell">
              <div class="user-name">{{ scope.row.name }}
                <i v-if="scope.row.gender === '男'" class="el-icon-male" style="color: #409EFF; margin-left:5px"></i>
                <i v-if="scope.row.gender === '女'" class="el-icon-female" style="color: #F56C6C; margin-left:5px"></i>
              </div>
              <div class="user-phone">{{ scope.row.phone }}</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="年龄" align="center" prop="age" width="60" />

        <el-table-column label="身体数据" align="center" width="160">
          <template slot-scope="scope">
            <div class="body-data">
              <el-tag size="mini" type="info" effect="plain">{{ scope.row.height }}cm</el-tag>
              <el-tag size="mini" type="info" effect="plain">{{ scope.row.weight }}kg</el-tag>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="卡类型" align="center" width="80">
          <template slot-scope="scope">
            <el-tag :type="getCardTagType(scope.row.cardType)" size="small" effect="dark">
              {{ scope.row.cardType }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="私教课时" align="center" width="100">
          <template slot-scope="scope">
             <span v-if="scope.row.classHours > 0" class="class-hours-active">
               {{ scope.row.classHours }} 节
             </span>
            <span v-else class="class-hours-empty">0</span>
          </template>
        </el-table-column>

        <el-table-column label="健身目标" align="center" prop="target" min-width="100" show-overflow-tooltip />

        <el-table-column label="到期时间" align="center" width="120">
          <template slot-scope="scope">
            <div>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</div>
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

    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body custom-class="custom-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">

        <div class="form-section-title">👤 基本信息</div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="会员卡号" prop="cardNo">
              <el-input v-model="form.cardNo" placeholder="自动生成" disabled prefix-icon="el-icon-bank-card" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" prefix-icon="el-icon-user" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input v-model="form.phone" placeholder="11位手机号" prefix-icon="el-icon-mobile-phone" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio label="男" border size="small">男</el-radio>
                <el-radio label="女" border size="small">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-section-title">💪 身体与目标</div>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="身高(cm)" prop="height">
              <el-input-number v-model="form.height" :min="100" :max="250" controls-position="right" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="体重(kg)" prop="weight">
              <el-input-number v-model="form.weight" :min="30" :max="200" controls-position="right" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="form.age" :min="1" :max="100" controls-position="right" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="目标" prop="specialty">
              <el-select v-model="form.specialty" placeholder="请选择目标" style="width: 100%" allow-create filterable default-first-option>
                <el-option v-for="item in specialtyOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-section-title">💳 办卡业务</div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="办卡类型" prop="cardType">
              <el-select v-model="form.cardType" placeholder="请选择" style="width: 100%" @change="calculateEndDate">
                <el-option label="次卡 (不限时)" value="次卡"></el-option>
                <el-option label="月卡 (30天)" value="月卡"></el-option>
                <el-option label="季卡 (90天)" value="季卡"></el-option>
                <el-option label="年卡 (365天)" value="年卡"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="私教课时" prop="classHours">
              <el-input v-model="form.classHours" placeholder="课时数" >
                <template slot="append">节</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%" @change="calculateEndDate"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" placeholder="自动计算" value-format="yyyy-MM-dd" style="width: 100%" readonly></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注信息" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="填写会员特殊情况..." />
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
import { listMember, getMember, delMember, addMember, updateMember } from "@/api/gym/member"

export default {
  name: "Member",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      memberList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cardNo: null,
        name: null, // 这里复用 name 字段作为综合搜索
        gender: null,
      },
      specialtyOptions: [
        { label: '增肌', value: '增肌' },
        { label: '减脂', value: '减脂' },
        { label: '塑形', value: '塑形' },
        { label: '瑜伽', value: '瑜伽' },
        { label: '普拉提', value: '普拉提' },
        { label: '康复训练', value: '康复训练' },
        { label: '拳击', value: '拳击' }
      ],
      form: {},
      rules: {
        name: [ { required: true, message: "必填", trigger: "blur" } ],
        phone: [ { required: true, message: "必填", trigger: "blur" }, { pattern: /^1[3-9]\d{9}$/, message: "格式错误", trigger: "blur" } ],
        gender: [ { required: true, message: "必选", trigger: "change" } ],
        cardType: [ { required: true, message: "必选", trigger: "change" } ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getCardTagType(type) {
      if (type === '年卡') return 'warning'; // 金色
      if (type === '季卡') return 'success'; // 绿色
      if (type === '月卡') return '';        // 蓝色
      return 'info';                         // 灰色
    },
    getList() {
      this.loading = true
      listMember(this.queryParams).then(response => {
        this.memberList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        memberId: null,
        cardNo: 'V' + Math.floor(10000 + Math.random() * 90000),
        name: null,
        phone: null,
        gender: "男",
        age: undefined,
        height: undefined,
        weight: undefined,
        target: '减脂',
        cardType: null,
        classHours: undefined,
        startDate: new Date().toISOString().slice(0, 10),
        endDate: null,
        remark: null
      }
      this.resetForm("form")
    },
    calculateEndDate() {
      if (!this.form.startDate || !this.form.cardType) return;
      let start = new Date(this.form.startDate);
      let end = new Date(start);
      switch (this.form.cardType) {
        case '次卡': break;
        case '周卡': end.setDate(end.getDate() + 7); break;
        case '月卡': end.setMonth(end.getMonth() + 1); break;
        case '季卡': end.setMonth(end.getMonth() + 3); break;
        case '年卡': end.setFullYear(end.getFullYear() + 1); break;
        default: return;
      }
      const y = end.getFullYear();
      const m = (end.getMonth() + 1).toString().padStart(2, '0');
      const d = end.getDate().toString().padStart(2, '0');
      this.form.endDate = `${y}-${m}-${d}`;
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.memberId) // 注意：如果你的主键是 id 请改成 item.id
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "✨ 新增会员"
    },
    handleUpdate(row) {
      this.reset();
      const id = row.memberId || this.ids;
      const memberId = Array.isArray(id) ? id[0] : id;
      getMember(memberId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "📝 编辑会员";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.memberId != null) { // 注意：如果主键是id，这里也要改
            updateMember(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMember(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.memberId || this.ids;
      this.$modal.confirm('确认删除？').then(() => {
        return delMember(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("已删除")
      }).catch(() => {})
    },
    handleExport() {
      this.download('gym/member/export', { ...this.queryParams }, `member_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped lang="scss">
.app-container {
  background-color: #f5f7fa; /* 整体背景变灰，突出卡片 */
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

/* 搜索框美化 */
/* 核心修改：增加了 .custom-round-input 限制，只让顶部的搜索框变圆 */
/* 弹窗里的普通输入框将恢复默认样式，完美适配右侧按钮和单位 */
::v-deep .custom-round-input .el-input__inner {
  border-radius: 20px;
}

/* 表格样式美化 */
.user-info-cell {
  display: flex;
  flex-direction: column;
  justify-content: center;
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
  background-color: #ecf5ff;
  color: #409EFF;
  padding: 4px 8px;
  border-radius: 4px;
  font-family: monospace;
  font-weight: bold;
  display: inline-block;
}

.body-data {
  display: flex;
  gap: 5px;
  justify-content: center;
}

.class-hours-active {
  color: #ff9900;
  font-weight: 800;
  font-size: 15px;
}
.class-hours-empty {
  color: #dcdfe6;
}

/* 弹窗样式 */
.form-section-title {
  font-size: 15px;
  font-weight: bold;
  color: #303133;
  margin: 10px 0 20px 0;
  padding-left: 10px;
  border-left: 4px solid #409EFF; /* 蓝色竖条装饰 */
}
</style>

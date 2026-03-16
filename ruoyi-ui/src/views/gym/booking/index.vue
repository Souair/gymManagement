<template>
  <div class="app-container">
    <el-card class="search-card" shadow="hover">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="70px">

        <el-form-item label="选择教练" prop="coachId">
          <el-select
            v-model="queryParams.coachId"
            placeholder="搜索教练姓名"
            clearable
            filterable
            style="width: 100%"
            @change="handleQuery">
            <el-option
              v-for="item in coachOptions"
              :key="item.coachId"
              :label="item.name + ' (' + item.phone + ')'"
              :value="item.coachId">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="选择会员" prop="memberId">
          <el-select
            v-model="queryParams.memberId"
            placeholder="搜索会员姓名"
            clearable
            filterable
            style="width: 100%"
            @change="handleQuery">
            <el-option
              v-for="item in memberOptions"
              :key="item.memberId"
              :label="item.name + ' (' + item.phone + ')'"
              :value="item.memberId"> </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="训练目标" prop="bookingType">
          <el-select v-model="queryParams.bookingType" placeholder="请选择" clearable style="width: 100%" @change="handleQuery">
            <el-option v-for="item in specialtyOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="预约时间" prop="bookingDate">
          <el-date-picker clearable
                          v-model="queryParams.bookingDate"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          format="yyyy-MM-dd HH:mm"
                          placeholder="选择具体时间点">
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card" shadow="hover" style="margin-top: 20px;">
      <div slot="header" class="clearfix">
        <span class="card-title">预约列表</span>
        <div style="float: right;">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['gym:booking:add']">新增预约</el-button>
          <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['gym:booking:edit']">修改</el-button>
          <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['gym:booking:remove']">删除</el-button>
          <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['gym:booking:export']">导出</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="bookingList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="预约ID" align="center" prop="id" width="80"/>

        <el-table-column label="教练姓名" align="center" prop="coachId">
          <template slot-scope="scope">
            {{ formatCoachName(scope.row.coachId) }}
          </template>
        </el-table-column>

        <el-table-column label="会员姓名" align="center" prop="memberId">
          <template slot-scope="scope">
            {{ formatMemberName(scope.row.memberId) }}
          </template>
        </el-table-column>

        <el-table-column label="训练目标" align="center" prop="bookingType">
          <template slot-scope="scope">
            <el-tag effect="plain">{{ scope.row.bookingType }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="预约时间" align="center" prop="bookingDate" width="160">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span>{{ parseTime(scope.row.bookingDate, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>

        <el-table-column label="时长(h)" align="center" prop="duration" width="80" />

        <el-table-column label="状态" align="center" prop="status">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status == '0'" type="warning">待开始</el-tag>
            <el-tag v-else-if="scope.row.status == '1'" type="success">已完成</el-tag>
            <el-tag v-else type="info">已取消</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip/>

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['gym:booking:edit']">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['gym:booking:remove']">删除</el-button>
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

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="选择教练" prop="coachId">
              <div style="display: flex; align-items: center">

                <el-select
                  v-model="form.coachId"
                  filterable
                  placeholder="请选择教练"
                  style="flex: 1">
                  <el-option
                    v-for="item in coachOptions"
                    :key="item.coachId"
                    :label="item.name"
                    :value="item.coachId">
                  </el-option>
                </el-select>

                <el-button
                  type="success"
                  icon="el-icon-cpu"
                  size="mini"
                  style="margin-left: 10px; flex-shrink: 0"
                  :loading="recommendLoading"
                  @click="handleAutoRecommend"
                >智能派单</el-button>

              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="选择会员" prop="memberId">
              <el-select v-model="form.memberId" filterable placeholder="请选择会员" style="width: 100%">
                <el-option
                  v-for="item in memberOptions"
                  :key="item.memberId"
                  :label="item.name"
                  :value="item.memberId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="预约时间" prop="bookingDate">
              <el-date-picker clearable
                              v-model="form.bookingDate"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              format="yyyy-MM-dd HH:mm"
                              placeholder="具体时间点"
                              style="width: 100%"
                              :picker-options="pickerOptions">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计时长(h)" prop="duration">
              <el-input-number v-model="form.duration" :precision="1" :step="0.5" :min="0.5" :max="5" label="小时"></el-input-number> 小时
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="训练目标" prop="bookingType">
              <el-select v-model="form.bookingType" placeholder="请选择目标" style="width: 100%">
                <el-option v-for="item in specialtyOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预约状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="0">待开始</el-radio>
                <el-radio label="1">已完成</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注信息" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入课程内容备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 合并了引用，看起来更整洁
import { listBooking, getBooking, delBooking, addBooking, updateBooking, recommendCoach } from "@/api/gym/booking"
import { listCoach } from "@/api/gym/coach"
import { listMember } from "@/api/gym/member"

export default {
  name: "Booking",
  data() {
    return {
      // 按钮loading状态
      recommendLoading: false,
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
      // 私教预约表格数据
      bookingList: [],
      // 教练列表选项
      coachOptions: [],
      // 会员列表选项
      memberOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        coachId: null,
        memberId: null,
        bookingType: null,
        bookingDate: null,
        status: null,
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
      // 👇 新增：日期选择器配置 (修正了这里)
      pickerOptions: {
        disabledDate(time) {
          // 禁止选择今天之前的日期
          return time.getTime() < Date.now() - 8.64e7;
        }
      }, // 👈 补上了这个逗号和括号！

      // 表单参数
      form: {},
      // 表单校验
      rules: {
        coachId: [
          { required: true, message: "请选择教练", trigger: "change" }
        ],
        memberId: [
          { required: true, message: "请选择会员", trigger: "change" }
        ],
        bookingType: [
          { required: true, message: "请选择训练目标", trigger: "change" }
        ],
        bookingDate: [
          { required: true, message: "预约时间不能为空", trigger: "blur" }
        ],
        duration: [
          { required: true, message: "时长不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList();
    this.getCoachList();
    this.getMemberList();
  },
  methods: {
    // --- 智能派单方法 ---
    handleAutoRecommend() {
      // 1. 校验前置条件
      if (!this.form.bookingType) {
        this.$modal.msgError("请先选择【训练目标】");
        return;
      }
      if (!this.form.bookingDate) {
        this.$modal.msgError("请先选择【预约时间】");
        return;
      }

      this.recommendLoading = true;

      const params = {
        specialty: this.form.bookingType,
        bookingDate: this.form.bookingDate,
        duration: this.form.duration || 1
      };

      recommendCoach(params).then(response => {
        this.recommendLoading = false;
        const coach = response.data;
        if (coach) {

          // =========== 👇 核心修改在这里 👇 ===========
          // 检查一下现在的下拉列表里，有没有这个教练？
          // 注意：我们要同时兼容 coachId 和 id 两种写法
          const exists = this.coachOptions.some(item =>
            (item.coachId === coach.coachId) || (item.id === coach.coachId)
          );

          // 如果列表里没有这个人（或者列表还没加载），我们手动把他 push 进去！
          if (!exists) {
            this.coachOptions.push(coach);
          }
          // ==========================================

          // 赋值 ID
          this.form.coachId = coach.coachId;

          this.$modal.msgSuccess(`系统已为您自动匹配教练：${coach.name}`);
        } else {
          this.$modal.msgWarning("未找到合适的教练，请手动选择");
        }
      }).catch(() => {
        this.recommendLoading = false;
      });
    },

    /** 查询私教预约列表 */
    getList() {
      this.loading = true
      listBooking(this.queryParams).then(response => {
        this.bookingList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 获取所有教练列表 */
    getCoachList() {
      // 查全部教练，不分页
      listCoach({ pageNum: 1, pageSize: 1000, status: '1' }).then(response => {
        this.coachOptions = response.rows;
      });
    },
    /** 获取所有会员列表 */
    getMemberList() {
      // 查全部会员，不分页
      listMember({ pageNum: 1, pageSize: 1000, status: '1' }).then(response => {
        this.memberOptions = response.rows;
      });
    },
    /** 辅助函数：根据ID显示教练名字 */
    formatCoachName(id) {
      const found = this.coachOptions.find(item => item.coachId === id);
      return found ? found.name : id; // 找不到显示ID
    },
    /** 辅助函数：根据ID显示会员名字 */
    formatMemberName(id) {
      const found = this.memberOptions.find(item => item.memberId === id);
      return found ? found.name : id;
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        coachId: null,
        memberId: null,
        bookingType: null,
        bookingDate: null, // 默认为空
        duration: 1.0,     // 默认1小时
        status: "0",       // 默认待开始
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "📅 新增预约"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getBooking(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "✏️ 修改预约"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBooking(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addBooking(this.form).then(response => {
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
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除该预约？').then(function() {
        return delBooking(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('gym/booking/export', {
        ...this.queryParams
      }, `booking_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.search-card, .table-card {
  border-radius: 8px;
  border: none;
}
.card-title {
  font-size: 16px;
  font-weight: bold;
}
</style>

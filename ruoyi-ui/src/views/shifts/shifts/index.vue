<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="班次名称" prop="shiftName">
        <el-input
          v-model="queryParams.shiftName"
          placeholder="请输入班次名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="starttime">
        <el-time-picker v-model="form.starttime" format="HH:mm" value-format="HH:mm"
          :style="{width: '100%'}" placeholder="请选择开始时间" clearable @change="onStartTimeChange"></el-time-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endtime">
        <el-time-picker v-model="form.endtime" format="HH:mm" value-format="HH:mm"
          :style="{width: '100%'}" placeholder="请选择结束时间" clearable :disabled="!form.starttime"></el-time-picker>
      </el-form-item>
      <el-form-item label="班次类型" prop="shiftType" style="width: 200px">
        <el-select v-model="queryParams.shiftType" placeholder="请选择班次类型" clearable>
          <el-option
            v-for="dict in layout_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['shifts:shifts:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['shifts:shifts:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['shifts:shifts:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['shifts:shifts:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shiftsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="班次编号" align="center" prop="id" />
      <el-table-column label="班次名称" align="center" prop="shiftName" />
      <el-table-column label="开始时间" align="center" prop="starttime" width="180">
        <template #default="scope">
          <span>{{ scope.row.starttime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endtime" width="180">
        <template #default="scope">
          <span>{{ scope.row.endtime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="班次类型" align="center" prop="shiftType">
        <template #default="scope">
          <dict-tag :options="layout_type" :value="scope.row.shiftType"/>
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="description" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['shifts:shifts:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['shifts:shifts:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改班次情况对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="shiftsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="班次名称" prop="shiftName">
          <el-input v-model="form.shiftName" placeholder="请输入班次名称" />
        </el-form-item>
        <el-form-item label="开始时间" prop="starttime">
          <el-time-picker v-model="form.starttime" format="HH:mm" value-format="HH:mm"
            :style="{width: '100%'}" placeholder="请选择开始时间" clearable @change="onStartTimeChange"></el-time-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endtime">
          <el-time-picker v-model="form.endtime" format="HH:mm" value-format="HH:mm"
            :style="{width: '100%'}" placeholder="请选择结束时间" clearable :disabled="!form.starttime"></el-time-picker>
        </el-form-item>
        <el-form-item label="班次类型" prop="shiftType">
          <el-select v-model="form.shiftType" placeholder="请选择班次类型">
            <el-option
              v-for="dict in layout_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Shifts">
import { listShifts, getShifts, delShifts, addShifts, updateShifts } from "@/api/shifts/shifts"

const { proxy } = getCurrentInstance()
const { layout_type } = proxy.useDict('layout_type')

const shiftsList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    shiftName: null,
    starttime: null,
    endtime: null,
    shiftType: null,
    description: null
  },
  rules: {
    shiftName: [
      { required: true, message: "班次名称不能为空", trigger: "blur" }
    ],
    shiftType: [
      { required: true, message: "班次类型不能为空", trigger: "change" }
    ],
    starttime: [
      { required: true, message: "开始时间不能为空", trigger: "change" }
    ],
    endtime: [
      { required: true, message: "结束时间不能为空", trigger: "change" },
      { validator: validateEndTime, trigger: "change" }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询班次情况列表 */
function getList() {
  loading.value = true
  listShifts(queryParams.value).then(response => {
    shiftsList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    shiftName: null,
    starttime: null,
    endtime: null,
    shiftType: null,
    description: null
  }
  proxy.resetForm("shiftsRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  timeRangeQuery.value = [];
  queryParams.value.starttime = null;
  queryParams.value.endtime = null;
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加班次情况"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getShifts(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改班次情况"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["shiftsRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateShifts(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addShifts(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除班次情况编号为"' + _ids + '"的数据项？').then(function() {
    return delShifts(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('shifts/shifts/export', {
    ...queryParams.value
  }, `shifts_${new Date().getTime()}.xlsx`)
}

// 开始时间变化时的处理函数
function onStartTimeChange(value) {
  // 如果开始时间被清空，也清空结束时间
  if (!value) {
    form.value.endtime = null;
  }
}

// 验证结束时间是否晚于开始时间
function validateEndTime(rule, value, callback) {
  if (value && form.value.starttime) {
    const startTime = form.value.starttime.replace(':', '');
    const endTime = value.replace(':', '');
    if (parseInt(endTime) <= parseInt(startTime)) {
      callback(new Error('结束时间必须晚于开始时间'));
    } else {
      callback();
    }
  } else {
    callback();
  }
}

getList()
</script>

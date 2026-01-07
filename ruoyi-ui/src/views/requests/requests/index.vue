<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="申请人" prop="requesterId">
        <el-select 
          v-model="queryParams.requesterId" 
          placeholder="请选择申请人"
          clearable
          filterable
          remote
          :remote-method="searchQueryUsers"
          :loading="userLoading"
          reserve-keyword
        >
          <el-option 
            v-for="item in queryUserList" 
            :key="item.userId" 
            :label="item.userName + '(' + item.nickName + ')'" 
            :value="item.userId"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="目标人" prop="targetId">
        <el-select 
          v-model="queryParams.targetId" 
          placeholder="请选择目标人"
          clearable
          filterable
          remote
          :remote-method="searchQueryUsers"
          :loading="userLoading"
          reserve-keyword
        >
          <el-option 
            v-for="item in queryUserList" 
            :key="item.userId" 
            :label="item.userName + '(' + item.nickName + ')'" 
            :value="item.userId"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="换/替班" prop="model" style="width: 240px;">
        <el-select 
          v-model="queryParams.model" 
          placeholder="请选择换班或替班" 
          clearable 
        >
          <el-option label="换班" :value="0"></el-option>
          <el-option label="替班" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="原排班" prop="requestedShiftId">
        <el-select 
          v-model="queryParams.requestedShiftId" 
          placeholder="请选择原排班"
          clearable
          filterable
          remote
          :remote-method="searchQueryShifts"
          :loading="shiftLoading"
          reserve-keyword
        >
          <el-option 
            v-for="item in queryShiftList" 
            :key="item.id" 
            :label="item.displayName" 
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="目标排班" prop="swappedShiftId">
        <el-select 
          v-model="queryParams.swappedShiftId" 
          placeholder="请选择目标排班"
          clearable
          filterable
          remote
          :remote-method="searchQueryShifts"
          :loading="shiftLoading"
          reserve-keyword
        >
          <el-option 
            v-for="item in queryShiftList" 
            :key="item.id" 
            :label="item.displayName" 
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="申请日期" prop="requestDate">
        <el-date-picker clearable
          v-model="queryParams.requestDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择申请日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option
            v-for="dict in layout_check"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="审批时间" prop="approvedAt">
        <el-date-picker clearable
          v-model="queryParams.approvedAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择审批时间">
        </el-date-picker>
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
          v-hasPermi="['requests:requests:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['requests:requests:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['requests:requests:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['requests:requests:export']"
        >导出</el-button>
      </el-col>
      <!-- 添加审核按钮，仅排班员和管理员可见 -->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Check"
          :disabled="single"
          @click="handleApprove"
          v-hasPermi="['requests:requests:approve']"
        >审核</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="requestsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="申请人" align="center" prop="requesterId">
        <template #default="scope">
          <span>{{ getUserDisplayName(scope.row.requesterId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="目标人" align="center" prop="targetId">
        <template #default="scope">
          <span>{{ getUserDisplayName(scope.row.targetId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="换/替班" align="center" prop="model">
        <template #default="scope">
          <span>{{ scope.row.model === 0 ? '换班' : scope.row.model === 1 ? '替班' : '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="原排班" align="center" prop="requestedShiftId">
        <template #default="scope">
          <span>{{ getShiftDisplayName(scope.row.requestedShiftId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="目标排班" align="center" prop="swappedShiftId">
        <template #default="scope">
          <span>{{ getShiftDisplayName(scope.row.swappedShiftId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请日期" align="center" prop="requestDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.requestDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="layout_check" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="申请原因" align="center" prop="reason" />
      <el-table-column label="审批人" align="center" prop="approvedBy">
        <template #default="scope">
          <span>{{ getUserDisplayName(scope.row.approvedBy) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审批时间" align="center" prop="approvedAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.approvedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['requests:requests:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['requests:requests:remove']">删除</el-button>
          <el-button link type="primary" icon="Check" @click="handleApprove(scope.row)" v-hasPermi="['requests:requests:approve']">审核</el-button>
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

    <!-- 添加或修改替换申请对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="requestsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="申请人" prop="requesterId" v-if="!hasRole(['common', 'leader'])">
          <el-select 
            v-model="form.requesterId" 
            placeholder="请选择申请人"
            clearable
            filterable
            remote
            :remote-method="searchUsers"
            :loading="userLoading"
            reserve-keyword
            @change="handleRequesterChange"
          >
            <el-option 
              v-for="item in userList" 
              :key="item.userId" 
              :label="item.userName + '(' + item.nickName + ')'" 
              :value="item.userId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目标人" prop="targetId">
          <el-select 
            v-model="form.targetId" 
            placeholder="请选择目标人"
            clearable
            filterable
            remote
            :remote-method="searchUsers"
            :loading="userLoading"
            reserve-keyword
            @change="handleTargetChange"
          >
            <el-option 
              v-for="item in userList" 
              :key="item.userId" 
              :label="item.userName + '(' + item.nickName + ')'" 
              :value="item.userId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择" prop="model">
          <el-radio-group v-model="form.model" @change="handleModelChange">
            <el-radio :value="0">换班</el-radio>
            <el-radio :value="1">替班</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="原排班" prop="requestedShiftId">
          <el-select 
            v-model="form.requestedShiftId" 
            placeholder="请选择原排班"
            clearable
            filterable
            :loading="shiftLoading"
            :disabled="form.model === 1"
            @change="handleRequestedShiftChange"
          >
            <el-option 
              v-for="item in form.requestedShiftOptions" 
              :key="item.id" 
              :label="item.displayName" 
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目标排班" prop="swappedShiftId">
          <el-select 
            v-model="form.swappedShiftId" 
            placeholder="请选择目标排班"
            clearable
            filterable
            :loading="shiftLoading"
            @change="handleSwappedShiftChange"
          >
            <el-option 
              v-for="item in form.swappedShiftOptions" 
              :key="item.id" 
              :label="item.displayName" 
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="申请日期" prop="requestDate">
          <el-date-picker clearable
            v-model="form.requestDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择申请日期"
            :readonly="hasRole(['common', 'leader'])">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="申请原因" prop="reason">
          <el-input v-model="form.reason" placeholder="请输入申请原因" />
        </el-form-item>
        <el-form-item label="状态" prop="status" v-if="!hasRole(['common', 'leader'])">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option
              v-for="dict in layout_check"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审批人" prop="approvedBy" v-if="!hasRole(['common', 'leader'])">
          <el-select 
            v-model="form.approvedBy" 
            placeholder="请选择审批人"
            clearable
            filterable
            remote
            :remote-method="searchUsers"
            :loading="userLoading"
            reserve-keyword
          >
            <el-option 
              v-for="item in userList" 
              :key="item.userId" 
              :label="item.userName + '(' + item.nickName + ')'" 
              :value="item.userId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审批时间" prop="approvedAt" v-if="!hasRole(['common', 'leader'])">
          <el-date-picker clearable
            v-model="form.approvedAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择审批时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog title="审核换班申请" v-model="approveOpen" width="500px" append-to-body>
      <el-form ref="approveRef" :model="approveForm" label-width="80px">
        <el-form-item label="申请人">
          <el-input v-model="approveForm.requesterName" disabled />
        </el-form-item>
        <el-form-item label="申请类型">
          <el-input :value="approveForm.model === 0 ? '换班' : approveForm.model === 1 ? '替班' : ''" disabled />
        </el-form-item>
        <el-form-item label="申请原因">
          <el-input v-model="approveForm.reason" type="textarea" disabled />
        </el-form-item>
        <el-form-item label="审核结果" prop="status">
          <el-radio-group v-model="approveForm.status">
            <el-radio value="1">批准</el-radio>
            <el-radio value="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="approveComment">
          <el-input v-model="approveForm.approveComment" type="textarea" placeholder="请输入审核意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitApprove">确 定</el-button>
          <el-button @click="cancelApprove">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Requests">
import { listRequests, getRequests, delRequests, addRequests, updateRequests } from "@/api/requests/requests"
import { listUser, getUser } from "@/api/system/user"
import { listEmployees } from "@/api/employees/employees"
import { listScheduling, getScheduling, addScheduling, updateScheduling } from "@/api/scheduling/scheduling"
import { listShifts, getShifts } from "@/api/shifts/shifts"

import useUserStore from '@/store/modules/user'

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")
const { layout_check } = proxy.useDict('layout_check')
const userStore = useUserStore()

// 添加角色判断函数
const hasRole = (roles) => {
  if (!userStore.roles) return false
  return roles.some(role => userStore.roles.includes(role))
}

const requestsList = ref([])
const open = ref(false)
const approveOpen = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const userLoading = ref(false)
const shiftLoading = ref(false)
const userList = ref([])
const shiftList = ref([])
const queryUserList = ref([])
const queryShiftList = ref([])
const userInfoCache = ref(new Map())
const shiftInfoCache = ref(new Map())
const schedulingMap = ref(new Map()) // 用于存储所有排班信息
const shiftMap = ref(new Map()) // 用于存储所有班次信息

const data = reactive({
  form: {},
  approveForm: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    requesterId: null,
    targetId: null,
    model: null,
    requestedShiftId: null,
    swappedShiftId: null,
    requestDate: null,
    status: null, 
    reason: null,
    approvedBy: null,
    approvedAt: null
  },
  rules: {
    requesterId: [{ required: true, message: "申请人不能为空", trigger: "blur" }],
    targetId: [
      { required: true, message: "目标人不能为空", trigger: "blur" },
      {
        validator: (rule, value, callback) => {
          if (form.value.model === 1 && form.value.requesterId && value && form.value.requesterId === value) {
            callback(new Error('替班时申请人和目标人不能相同'));
          } else {
            callback();
          }
        },
        trigger: "blur"
      }
    ],
    model: [{ required: true, message: "换班/替班选项不能为空", trigger: "blur" }],
    requestedShiftId: [
      { 
        required: true, 
        message: "原班次不能为空", 
        trigger: "blur",
        validator: (rule, value, callback) => {
          if (form.value.model === 0 && !value) {
            callback(new Error('原班次不能为空'));
          } else {
            // 检查原排班和目标排班是否相同
            if (form.value.model === 0 && value && form.value.swappedShiftId && value === form.value.swappedShiftId) {
              callback(new Error('原排班和目标排班不能相同'));
            } else {
              callback();
            }
          }
        }
      }
    ],
    swappedShiftId: [
      { required: true, message: "目标班次不能为空", trigger: "blur" },
      {
        validator: (rule, value, callback) => {
          // 检查原排班和目标排班是否相同
          if (form.value.model === 0 && value && form.value.requestedShiftId && value === form.value.requestedShiftId) {
            callback(new Error('原排班和目标排班不能相同'));
          } else {
            callback();
          }
        },
        trigger: "blur"
      }
    ],
    requestDate: [{ required: true, message: "申请日期不能为空", trigger: "blur" }],
    reason: [{ required: true, message: "申请原因不能为空", trigger: "blur" }],
    status: [{ required: true, message: "状态不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules, approveForm } = toRefs(data)

/** 查询替换申请列表 */
function getList() {
  loading.value = true
  // 预先加载所有排班和班次信息
  Promise.all([loadAllScheduling(), loadAllShifts()]).then(() => {
    // 如果用户角色是普通员工(common)，则只显示申请人是当前用户的记录
    const params = { ...queryParams.value };
    if (hasRole(['common'])) {
      params.requesterId = userStore.id;
    }
    
    listRequests(params).then(response => {
      requestsList.value = response.rows
      total.value = response.total
      loading.value = false
      
      // 预加载用户信息
      response.rows.forEach(item => {
        // 预加载用户信息
        if (item.requesterId && !userInfoCache.value.has(item.requesterId)) {
          getUser(item.requesterId).then(res => {
            userInfoCache.value.set(item.requesterId, res.data)
            requestsList.value = [...requestsList.value]
          })
        }
        
        if (item.targetId && !userInfoCache.value.has(item.targetId)) {
          getUser(item.targetId).then(res => {
            userInfoCache.value.set(item.targetId, res.data)
            requestsList.value = [...requestsList.value]
          })
        }
        
        if (item.approvedBy && !userInfoCache.value.has(item.approvedBy)) {
          getUser(item.approvedBy).then(res => {
            userInfoCache.value.set(item.approvedBy, res.data)
            requestsList.value = [...requestsList.value]
          })
        }
      })
    })
  })
}

// 获取所有排班信息
function loadAllScheduling() {
  return listScheduling({ pageSize: 1000 }).then(response => {
    response.rows.forEach(scheduling => {
      schedulingMap.value.set(scheduling.id, scheduling)
    })
  })
}

// 获取所有班次信息
function loadAllShifts() {
  return listShifts({ pageSize: 1000 }).then(response => {
    response.rows.forEach(shift => {
      shiftMap.value.set(shift.id, shift)
    })
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 取消审核
function cancelApprove() {
  approveOpen.value = false
  resetApprove()
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    requesterId: null,
    targetId: null,
    model: null,
    requestedShiftId: null,
    swappedShiftId: null,
    requestDate: null,
    status: "0", 
    reason: null,
    approvedBy: null,
    approvedAt: null,
    // 添加排班选项列表
    requestedShiftOptions: [],
    swappedShiftOptions: []
  }
  proxy.resetForm("requestsRef")
}

// 审核表单重置
function resetApprove() {
  approveForm.value = {
    id: null,
    requesterName: null,
    model: null,
    reason: null,
    status: "1",
    approveComment: null
  }
  proxy.resetForm("approveRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  queryUserList.value = []
  queryShiftList.value = []
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
  // 设置默认值：当前用户为申请人，当前日期为申请日期
  form.value.requesterId = userStore.id
  form.value.requestDate = new Date().toISOString().slice(0, 10)
  open.value = true
  title.value = "添加替换申请"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getRequests(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改替换申请"
    
    // 加载申请人和目标人的排班信息
    if (response.data.requesterId) {
      loadUserSchedulings(response.data.requesterId, 'requested');
    }
    
    if (response.data.targetId) {
      loadUserSchedulings(response.data.targetId, 'swapped');
    }
  })
}

/** 审核按钮操作 */
function handleApprove(row) {
  resetApprove()
  const _id = row.id || ids.value
  getRequests(_id).then(response => {
    const requestData = response.data
    approveForm.value.id = requestData.id
    approveForm.value.model = requestData.model
    approveForm.value.reason = requestData.reason
    approveForm.value.requesterId = requestData.requesterId // 保存申请人ID用于后续通知
    approveOpen.value = true
    
    // 获取申请人显示名称
    if (requestData.requesterId) {
      if (userInfoCache.value.has(requestData.requesterId)) {
        // 从缓存中获取用户信息
        const user = userInfoCache.value.get(requestData.requesterId)
        approveForm.value.requesterName = `${user.userName}(${user.nickName})`
      } else {
        // 从服务器获取用户信息
        getUser(requestData.requesterId).then(res => {
          const user = res.data
          userInfoCache.value.set(requestData.requesterId, user)
          approveForm.value.requesterName = `${user.userName}(${user.nickName})`
        }).catch(() => {
          approveForm.value.requesterName = '未知申请人'
        })
      }
    } else {
      approveForm.value.requesterName = '未知申请人'
    }
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["requestsRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateRequests(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addRequests(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
          
  
        })
      }
    }
  })
}

/** 提交审核 */
function submitApprove() {
  proxy.$refs["approveRef"].validate(valid => {
    if (valid) {
      
      const updateData = {
        id: approveForm.value.id,
        status: approveForm.value.status,
        approvedBy: userStore.id, // 当前审核人
        approvedAt: new Date().toISOString().slice(0, 10) // 当前日期
      }
      
      updateRequests(updateData).then(response => {
        proxy.$modal.msgSuccess("审核成功")
        approveOpen.value = false
        getList()
        
        // 如果审核通过，更新排班
        if (approveForm.value.status === '1') {
          updateSchedulingAfterApproval(approveForm.value);
        }
        

      }).catch(error => {
        console.error("审核提交失败:", error);
        proxy.$modal.msgError("审核失败")
      })
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除替换申请编号为"' + _ids + '"的数据项？').then(function() {
    return delRequests(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('requests/requests/export', {
    ...queryParams.value
  }, `requests_${new Date().getTime()}.xlsx`)
}

// 获取用户显示名称
function getUserDisplayName(userId) {
  if (!userId) return ''
  
  if (userInfoCache.value.has(userId)) {
    const user = userInfoCache.value.get(userId)
    return `${user.userName}(${user.nickName})`
  }
  
  // 获取用户信息
  getUser(userId).then(response => {
    const user = response.data
    userInfoCache.value.set(userId, user)
    // 触发重新渲染
    requestsList.value = [...requestsList.value]
  }).catch(() => {
    return userId
  })
  
  return userId
}

// 获取班次显示名称
function getShiftDisplayName(schedulingId) {
  if (!schedulingId) return ''
  
  const scheduling = schedulingMap.value.get(schedulingId)
  if (scheduling) {
    const shift = shiftMap.value.get(scheduling.shiftId)
    if (shift) {
      return `${scheduling.id}(${shift.shiftName})`
    }
    return `${scheduling.id})未知班次`
  }
  
  return '未知排班'
}

// 搜索用户
function searchUsers(queryString) {
  if (!queryString) {
    userList.value = []
    return
  }
  
  userLoading.value = true
  listEmployees({
    pageSize: 20
  }).then(response => {
    // 获取员工的用户信息
    const userIds = response.rows.map(employee => employee.userId);
    
    // 批量获取用户信息
    const userPromises = userIds.map(userId => {
      if (userId && !userInfoCache.value.has(userId)) {
        return getUser(userId).then(userRes => {
          userInfoCache.value.set(userId, userRes.data);
        }).catch(() => {
          // 获取用户信息失败，使用默认值
          userInfoCache.value.set(userId, { 
            userId: userId, 
            userName: '未知用户', 
            nickName: '' 
          });
        });
      }
      return Promise.resolve();
    });
    
    Promise.all(userPromises).then(() => {
      // 构建用户列表
      userList.value = response.rows.map(employee => {
        const user = userInfoCache.value.get(employee.userId);
        return user || { userId: employee.userId, userName: '未知用户', nickName: '' };
      });
      userLoading.value = false;
    });
  }).catch(() => {
    userLoading.value = false
  })
}

// 搜索排班（已废弃，现在根据用户动态加载）
function searchShifts(queryString) {
  // 不再使用此函数，排班现在根据选定的用户动态加载
  console.warn('searchShifts function is deprecated. Schedulings are now loaded dynamically based on selected users.');
}

// 搜索用户（用于查询条件）
function searchQueryUsers(queryString) {
  if (!queryString) {
    queryUserList.value = []
    return
  }
  
  userLoading.value = true
  listEmployees({
    pageSize: 20
  }).then(response => {
    // 获取员工的用户信息
    const userIds = response.rows.map(employee => employee.userId);
    
    // 批量获取用户信息
    const userPromises = userIds.map(userId => {
      if (userId && !userInfoCache.value.has(userId)) {
        return getUser(userId).then(userRes => {
          userInfoCache.value.set(userId, userRes.data);
        }).catch(() => {
          // 获取用户信息失败，使用默认值
          userInfoCache.value.set(userId, { 
            userId: userId, 
            userName: '未知用户', 
            nickName: '' 
          });
        });
      }
      return Promise.resolve();
    });
    
    Promise.all(userPromises).then(() => {
      // 构建用户列表
      queryUserList.value = response.rows.map(employee => {
        const user = userInfoCache.value.get(employee.userId);
        return user || { userId: employee.userId, userName: '未知用户', nickName: '' };
      });
      userLoading.value = false;
    });
  }).catch(() => {
    userLoading.value = false
  })
}

// 搜索排班（用于查询条件）
function searchQueryShifts(queryString) {
  shiftLoading.value = true
  
  // 获取所有排班数据
  const allSchedulings = Array.from(schedulingMap.value.values())
  
  if (!queryString) {
    // 如果没有搜索关键字，显示所有排班
    const result = allSchedulings.map(scheduling => {
      const shift = shiftMap.value.get(scheduling.shiftId)
      return {
        ...scheduling,
        displayName: shift ? `${scheduling.id}(${shift.shiftName})` : `${scheduling.id}(未知班次)`
      }
    })
    
    queryShiftList.value = result
    shiftLoading.value = false
    return
  }
  
  // 有过滤条件时进行过滤
  let filteredSchedulings = allSchedulings.filter(scheduling => {
    // 通过排班ID搜索
    if (scheduling.id.toString().includes(queryString)) {
      return true
    }
    
    // 通过班次名称搜索
    const shift = shiftMap.value.get(scheduling.shiftId)
    if (shift && shift.shiftName.includes(queryString)) {
      return true
    }
    
    return false
  })
  
  // 为每个排班添加显示标签
  const result = filteredSchedulings.map(scheduling => {
    const shift = shiftMap.value.get(scheduling.shiftId)
    return {
      ...scheduling,
      displayName: shift ? `${scheduling.id}(${shift.shiftName})` : `${scheduling.id}(未知班次)`
    }
  })
  
  queryShiftList.value = result
  shiftLoading.value = false
}

// 处理换班/替班选择变化
function handleModelChange(value) {
  // 当切换为替班时，清空原排班
  if (value === 1) {
    form.value.requestedShiftId = null;
    // 清除原排班字段的验证错误
    proxy.$refs.requestsRef.clearValidate('requestedShiftId');
  }
  // 清除目标排班和目标人的验证错误
  proxy.$refs.requestsRef.clearValidate('swappedShiftId');
  proxy.$refs.requestsRef.clearValidate('targetId');
  
  // 如果是换班并且已有申请人，重新加载申请人的排班
  if (value === 0 && form.value.requesterId) {
    loadUserSchedulings(form.value.requesterId, 'requested');
  }
  
  // 如果已有目标人，重新加载目标人的排班
  if (form.value.targetId) {
    loadUserSchedulings(form.value.targetId, 'swapped');
  }
}

// 处理原排班变化
function handleRequestedShiftChange(value) {
  // 清除目标排班的验证错误
  proxy.$refs.requestsRef.clearValidate('swappedShiftId');
}

// 处理目标排班变化
function handleSwappedShiftChange(value) {
  // 清除原排班的验证错误
  proxy.$refs.requestsRef.clearValidate('requestedShiftId');
}

// 处理申请人变化
function handleRequesterChange(value) {
  // 清除目标人的验证错误
  proxy.$refs.requestsRef.clearValidate('targetId');
  
  // 当申请人改变时，清空原排班选择
  form.value.requestedShiftId = null;
  
  // 如果有申请人，加载该申请人的排班信息
  if (value) {
    loadUserSchedulings(value, 'requested');
  }
}

// 处理目标人变化
function handleTargetChange(value) {
  // 清除目标人的验证错误
  proxy.$refs.requestsRef.clearValidate('targetId');
  
  // 当目标人改变时，清空目标排班选择
  form.value.swappedShiftId = null;
  
  // 如果有目标人，加载该目标人的排班信息
  if (value) {
    loadUserSchedulings(value, 'swapped');
  }
}

// 根据用户ID加载其排班信息
function loadUserSchedulings(userId, type) {
  if (!userId) return;
  
  shiftLoading.value = true;
  listScheduling({
    userId: userId
  }).then(response => {
    // 获取排班数据并映射显示名称，排除状态为1的排班
    const schedulings = response.rows
      .filter(scheduling => scheduling.status !== '1') // 排除状态为1（已取消）的排班
      .map(scheduling => {
        const shift = shiftMap.value.get(scheduling.shiftId);
        return {
          ...scheduling,
          displayName: shift ? `${scheduling.id}(${shift.shiftName})` : `${scheduling.id}(未知班次)`
        };
      });
    
    // 根据类型设置对应的排班列表
    if (type === 'requested') {
      // 原排班列表
      form.value.requestedShiftOptions = schedulings;
    } else if (type === 'swapped') {
      // 目标排班列表
      form.value.swappedShiftOptions = schedulings;
    }
    
    shiftLoading.value = false;
  }).catch(() => {
    shiftLoading.value = false;
  });
}

// 审核通过后更新排班
function updateSchedulingAfterApproval(requestData) {
  // 获取完整的申请信息
  getRequests(requestData.id).then(response => {
    const fullRequestData = response.data;
    
    // 根据申请类型处理不同的排班更新逻辑
    if (fullRequestData.model === 0) { // 换班
      // 换班逻辑：交换申请人和目标人的排班
      handleSwapShifts(fullRequestData);
    } else if (fullRequestData.model === 1) { // 替班
      // 替班逻辑：将申请人的排班更新为目标排班
      handleSubstituteShifts(fullRequestData);
    }
  }).catch(error => {
    console.error("获取申请详细信息失败:", error);
    proxy.$modal.msgError("更新排班失败");
  });
}

// 处理换班逻辑
function handleSwapShifts(requestData) {
  // 获取原排班和目标排班的详细信息
  Promise.all([
    getScheduling(requestData.requestedShiftId), // 原排班
    getScheduling(requestData.swappedShiftId)     // 目标排班
  ]).then(([originalResponse, targetResponse]) => {
    const originalScheduling = originalResponse.data;
    const targetScheduling = targetResponse.data;
    
    // 交换排班的员工ID
    const updatedOriginalScheduling = {
      ...originalScheduling,
      userId: targetScheduling.userId // 原排班现在分配给目标人的员工
    };
    
    const updatedTargetScheduling = {
      ...targetScheduling,
      userId: originalScheduling.userId // 目标排班现在分配给原排班的员工
    };
    
    // 更新两个排班记录
    Promise.all([
      updateScheduling(updatedOriginalScheduling),
      updateScheduling(updatedTargetScheduling)
    ]).then(() => {
      proxy.$modal.msgSuccess("换班排班更新成功");
      // 刷新排班列表
      if (getList) getList();
    }).catch(error => {
      console.error("更新排班失败:", error);
      proxy.$modal.msgError("更新排班失败");
    });
  }).catch(error => {
    console.error("获取排班信息失败:", error);
    proxy.$modal.msgError("获取排班信息失败");
  });
}

// 处理替班逻辑
function handleSubstituteShifts(requestData) {
  // 获取目标排班的详细信息
  getScheduling(requestData.swappedShiftId).then(response => {
    const targetScheduling = response.data;
    
    // 获取申请人当前日期的排班信息（如果存在）
    listScheduling({
      userId: requestData.requesterId,
      scheduleDate: targetScheduling.scheduleDate,
      pageSize: 1
    }).then(schedulingResponse => {
      if (schedulingResponse.rows && schedulingResponse.rows.length > 0) {
        // 如果申请人当天已有排班，更新该排班
        const applicantScheduling = schedulingResponse.rows[0];
        const updatedScheduling = {
          ...applicantScheduling,
          shiftId: targetScheduling.shiftId
        };
        
        updateScheduling(updatedScheduling).then(() => {
          proxy.$modal.msgSuccess("替班排班更新成功");
          // 刷新排班列表
          if (getList) getList();
        }).catch(error => {
          console.error("更新申请人排班失败:", error);
          proxy.$modal.msgError("更新申请人排班失败");
        });
      } else {
        // 如果申请人当天没有排班，创建新的排班记录
        const newScheduling = {
          userId: requestData.requesterId,
          shiftId: targetScheduling.shiftId,
          scheduleDate: targetScheduling.scheduleDate,
          status: '2' // 默认状态
        };
        
        addScheduling(newScheduling).then(() => {
          proxy.$modal.msgSuccess("替班排班创建成功");
          // 刷新排班列表
          if (getList) getList();
        }).catch(error => {
          console.error("创建申请人排班失败:", error);
          proxy.$modal.msgError("创建申请人排班失败");
        });
      }
    }).catch(error => {
      console.error("查询申请人排班失败:", error);
      proxy.$modal.msgError("查询申请人排班失败");
    });
  }).catch(error => {
    console.error("获取目标排班信息失败:", error);
    proxy.$modal.msgError("获取目标排班信息失败");
  });
}

getList()
</script>
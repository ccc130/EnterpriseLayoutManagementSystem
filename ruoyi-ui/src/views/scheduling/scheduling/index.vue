<template>
  <div class="app-container">
    <el-form v-if="!visualMode" :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="员工" prop="userId">
        <el-autocomplete
          v-model="queryParams.userId"
          placeholder="请输入员工姓名"
          clearable
          :fetch-suggestions="queryUsers"
          :trigger-on-focus="false"
          @select="handleUserSelect"
          @keyup.enter="handleQuery"
        >
          <template #default="{ item }">
            <div>{{ item.userName }}({{ item.nickName }})</div>
          </template>
        </el-autocomplete>
      </el-form-item>
      <el-form-item label="班次" prop="shiftId">
        <el-autocomplete
          v-model="queryParams.shiftId"
          placeholder="请输入班次名称"
          clearable
          :fetch-suggestions="queryShifts"
          :trigger-on-focus="false"
          @select="handleShiftSelect"
          @keyup.enter="handleQuery"
        >
          <template #default="{ item }">
            <div>{{ item.shiftName }}</div>
          </template>
        </el-autocomplete>
      </el-form-item>
      <el-form-item label="排班日期" style="width: 308px">
        <el-date-picker
          v-model="daterangeScheduleDate"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="状态" prop="status" style="width: 308px">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in layout_status"
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
      <el-col  v-if="!visualMode":span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['scheduling:scheduling:add']"
        >新增</el-button>
      </el-col>
      <el-col  v-if="!visualMode":span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['scheduling:scheduling:remove']"
        >修改</el-button>
      </el-col>
      <el-col  v-if="!visualMode":span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['scheduling:scheduling:remove']"
        >删除</el-button>
      </el-col>
      <el-col  v-if="!visualMode" :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['scheduling:scheduling:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="View"
          @click="toggleVisualMode"
        >{{ visualMode ? '列表视图' : '可视化视图' }}</el-button>
        </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-if="!visualMode" v-loading="loading" :data="schedulingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="排班编号" align="center" prop="id" />
      <el-table-column label="员工姓名" align="center" prop="userId">
        <template #default="scope">
          <span>{{ getUserDisplayInTable(scope.row.userId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="班次名称" align="center" prop="shiftId">
        <template #default="scope">
          <span>{{ getShiftDisplayInTable(scope.row.shiftId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="排班日期" align="center" prop="scheduleDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.scheduleDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="layout_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remarks" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['scheduling:scheduling:remove']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['scheduling:scheduling:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination v-if="!visualMode"
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改排班情况对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="schedulingRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="员工" prop="userId">
          <el-select 
            v-model="form.userId" 
            placeholder="请选择员工"
            clearable
            filterable
            remote
            :remote-method="searchUsers"
            :loading="userLoading"
            reserve-keyword
            :disabled="isReadOnly"
          >
            <el-option 
              v-for="item in userList" 
              :key="item.userId" 
              :label="item.userName + '(' + item.nickName  + ')'" 
              :value="item.userId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="班次" prop="shiftId">
          <el-select 
            v-model="form.shiftId" 
            placeholder="请选择班次"
            clearable
            filterable
            remote
            :remote-method="searchShifts"
            :loading="shiftLoading"
            reserve-keyword
            :disabled="isReadOnly"
          >
            <el-option 
              v-for="item in shiftList" 
              :key="item.id" 
              :label="item.id + '(' + item.shiftName  + ')'" 
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排班日期" prop="scheduleDate">
          <el-date-picker clearable
            v-model="form.scheduleDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择排班日期"
            :disabled="isReadOnly">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" :disabled="isReadOnly">
            <el-option
              v-for="dict in layout_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" placeholder="请输入备注" :disabled="isReadOnly" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm" v-if="!isReadOnly">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 可视化排班界面 -->
  <div v-show="visualMode" class="visual-scheduling-container">
    <el-card class="visual-header">
      <div class="header-controls">
        <el-date-picker
          v-model="visualMonth"
          type="month"
          placeholder="选择月份"
          format="YYYY年MM月"
          value-format="YYYY-MM"
          @change="loadVisualData"
        ></el-date-picker>
        <el-button type="primary" @click="loadVisualData">刷新</el-button>
        <el-button type="success" @click="exportVisualAsImage" :loading="exportLoading">
          导出为图片
        </el-button>
      </div>
    </el-card>
   
   <el-table 
     :data="visualSchedulingData" 
     border 
     class="visual-table"
     :loading="visualLoading"
   >
     <el-table-column prop="userName" label="员工" fixed width="120">
       <template #default="scope">
         <div class="employee-info">
           <div>{{ scope.row.userName }}</div>
           <div class="nick-name">{{ scope.row.nickName }}</div>
         </div>
       </template>
     </el-table-column>
     <el-table-column
       v-for="day in daysInMonth"
       :key="day.date"
       :prop="'day' + day.day"
       :label="day.day.toString()"
       width="80"
       align="center"
     >
       <template #default="scope">
         <div 
           class="shift-cell"
           :class="[getShiftClass(scope.row['day' + day.day]), 'editable-cell']"
           @click="handleCellClick(scope.row, day, scope.row['day' + day.day])"
         >
           <el-tooltip 
             v-if="scope.row['day' + day.day]" 
             :content="getShiftTooltip(scope.row['day' + day.day])" 
             placement="top"
           >
             <div class="shift-content">
               <div class="shift-name">{{ getShiftDisplay(scope.row['day' + day.day]) }}</div>
               <div v-if="isCurrentUser(scope.row.userId) && scope.row['day' + day.day].status !== '0'" class="confirm-btn" @click.stop="confirmShift(scope.row, day)">确认</div>
               <div v-else-if="scope.row['day' + day.day].status === '0'" class="confirmed-text">已确认</div>
             </div>
           </el-tooltip>
           <span v-else class="add-symbol">+</span>
         </div>
       </template>
     </el-table-column>
   </el-table>
 </div>
  </div>
</template>

<script setup name="Scheduling">
import { listScheduling, getScheduling, delScheduling, addScheduling, updateScheduling } from "@/api/scheduling/scheduling"
import { listUser, getUser } from "@/api/system/user"
import { listEmployees } from "@/api/employees/employees"
import { listShifts } from "@/api/shifts/shifts"
import dayjs from 'dayjs'
import useUserStore from '@/store/modules/user'

const { proxy } = getCurrentInstance()
const { layout_status } = proxy.useDict('layout_status')
const shiftInfoCache = ref(new Map())

// 获取当前用户ID
const currentUserId = ref(null)

// 获取当前用户ID
onMounted(() => {
  // 从store获取当前用户信息
  const userStore = useUserStore()
  currentUserId.value = userStore.id
})

// 判断是否为当前用户
function isCurrentUser(userId) {
  return currentUserId.value === userId
}
// 获取班次显示名称
function getShiftDisplayInTable(shiftId) {
  if (shiftInfoCache.value.has(shiftId)) {
    const shift = shiftInfoCache.value.get(shiftId)
    return shift.shiftName
  }
  return shiftId
}
/** 查询排班情况列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeScheduleDate && '' != daterangeScheduleDate) {
    queryParams.value.params["beginScheduleDate"] = daterangeScheduleDate.value[0]
    queryParams.value.params["endScheduleDate"] = daterangeScheduleDate.value[1]
  }
  // 确保获取所有数据，而不是分页数据
  const params = { ...queryParams.value, pageSize: 10000 }
  listScheduling(params).then(response => {
    schedulingList.value = response.rows
    total.value = response.total
    loading.value = false

    response.rows.forEach(item => {
      if (item.userId && !userInfoCache.value.has(item.userId)) {
        getUser(item.userId).then(res => {
          userInfoCache.value.set(item.userId, res.data)
          schedulingList.value = [...schedulingList.value]
        })
      }
      
      if (item.shiftId && !shiftInfoCache.value.has(item.shiftId)) {
        getShiftInfo(item.shiftId)
      }
    })
  })
}

// 获取班次详细信息
function getShiftInfo(shiftId) {
  listShifts({ id: shiftId }).then(response => {
    if (response.rows && response.rows.length > 0) {
      const shift = response.rows[0]
      shiftInfoCache.value.set(shiftId, shift)
      schedulingList.value = [...schedulingList.value]
    }
  }).catch(() => {
    return shiftId
  })
}

// 获取员工信息
function getUserDisplayInTable(userId) {
  if (userInfoCache.value.has(userId)) {
    const user = userInfoCache.value.get(userId)
    return `${user.userName}(${user.nickName})`
  }
  
  if (userId) {
    getUser(userId).then(response => {
      const user = response.data
      userInfoCache.value.set(userId, user)
      schedulingList.value = [...schedulingList.value]
    }).catch(() => {
      return userId
    })
  }
  return userId
}

const schedulingList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const daterangeScheduleDate = ref([])
const userLoading = ref(false)
const shiftLoading = ref(false)
const userList = ref([])
const shiftList = ref([])
const userInfoCache = ref(new Map())

// 可视化界面相关
 const visualMode = ref(false)
 const visualLoading = ref(false)
 const visualMonth = ref(dayjs().format('YYYY-MM'))
 const visualSchedulingData = ref([])
 const daysInMonth = ref([])
 const shiftMap = ref(new Map())

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userId: null,
    shiftId: null,
    scheduleDate: null,
    status: null,
  },
  rules: {
    userId: [
      { required: true, message: "员工不能为空", trigger: "blur" }
    ],
    shiftId: [
      { required: true, message: "班次不能为空", trigger: "blur" }
    ],
    scheduleDate: [
      { required: true, message: "排班日期不能为空", trigger: "blur" }
    ]
  }
})

const isReadOnly = ref(false);

const { queryParams, form, rules } = toRefs(data)


// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    userId: null,
    shiftId: null,
    scheduleDate: null,
    status: "2",
    remarks: null
  }
  userList.value = []
  shiftList.value = []
  proxy.resetForm("schedulingRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeScheduleDate.value = []
  proxy.resetForm("queryRef")
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
  title.value = "添加排班情况"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getScheduling(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改排班情况"
    isReadOnly.value = false
  })
}

/** 查看按钮操作 */
function handleView(row) {
  reset()
  const _id = row.id || ids.value
  getScheduling(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "查看排班情况"
    isReadOnly.value = true
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["schedulingRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateScheduling(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
          // 如果当前在可视化模式，刷新可视化数据
          if (visualMode.value) {
            loadVisualData()
          }
        })
      } else {
        addScheduling(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
          // 如果当前在可视化模式，刷新可视化数据
          if (visualMode.value) {
            loadVisualData()
          }
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除排班情况编号为"' + _ids + '"的数据项？').then(function() {
    return delScheduling(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
    // 如果当前在可视化模式，刷新可视化数据
    if (visualMode.value) {
      loadVisualData()
    }
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('scheduling/scheduling/export', {
    ...queryParams.value
  }, `scheduling_${new Date().getTime()}.xlsx`)
}

// 查询用户建议
function queryUsers(queryString, cb) {
  if (!queryString) {
    cb([]);
    return;
  }
  
  listEmployees({
    pageSize: 20
  }).then(response => {
    // 获取所有员工的用户信息
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
      // 构造返回数据
      const employees = response.rows.map(employee => {
        const user = userInfoCache.value.get(employee.userId);
        return {
          value: employee.userId,
          userName: user ? user.userName : '未知用户',
          nickName: user ? user.nickName : ''
        };
      });
      cb(employees);
    });
  }).catch(() => {
    cb([]);
  });
}

// 处理用户选择
function handleUserSelect(item) {
  queryParams.value.userId = item.value;
}

// 查询班次建议
function queryShifts(queryString, cb) {
  if (!queryString) {
    cb([]);
    return;
  }
  
  listShifts({
    shiftName: queryString,
    pageSize: 20
  }).then(response => {
    const shifts = response.rows.map(shift => ({
      value: shift.id,
      shiftName: shift.shiftName
    }));
    cb(shifts);
  }).catch(() => {
    cb([]);
  });
}

// 处理班次选择
function handleShiftSelect(item) {
  queryParams.value.shiftId = item.value;
}

/** 搜索用户 */
function searchUsers(query) {
  userLoading.value = true;
  listEmployees({
    pageSize: 100
  }).then(response => {
    // 获取所有员工的用户信息
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
      // 获取员工的用户信息
      userList.value = response.rows.map(employee => {
        const user = userInfoCache.value.get(employee.userId);
        return user || { userId: employee.userId, userName: '未知用户', nickName: '' };
      });
      userLoading.value = false;
    });
  }).catch(() => {
    userLoading.value = false;
  });
}

/** 搜索班次 */
function searchShifts(query) {
  shiftLoading.value = true;
  listShifts({
    shiftName: query,
    pageSize: 100
  }).then(response => {
    shiftList.value = response.rows;
    shiftLoading.value = false;
  }).catch(() => {
    shiftLoading.value = false;
  });
}

// 切换视图模式
function toggleVisualMode() {
  visualMode.value = !visualMode.value
  if (visualMode.value) {
    loadVisualData()
    startAutoRefresh() // 进入可视化模式时开启定时刷新
  } else {
    stopAutoRefresh() // 离开可视化模式时停止定时刷新
  }
}

// 加载可视化数据
function loadVisualData() {
  visualLoading.value = true
  
  // 获取当月所有天数
  generateDaysInMonth()
  
  // 获取该月的所有排班数据
  const beginDate = `${visualMonth.value}-01`
  const endDate = dayjs(`${visualMonth.value}-01`).endOf('month').format('YYYY-MM-DD')
  
  // 使用大pageSize确保获取所有数据
  listScheduling({
    pageSize: 10000,
    params: {
      beginScheduleDate: beginDate,
      endScheduleDate: endDate
    }
  }).then(response => {
    processVisualData(response.rows)
    visualLoading.value = false
  }).catch(() => {
    visualLoading.value = false
  })
}

// 生成月份中的所有天数
function generateDaysInMonth() {
  const year = parseInt(visualMonth.value.split('-')[0])
  const month = parseInt(visualMonth.value.split('-')[1]) - 1
  const days = dayjs(`${year}-${month + 1}-01`).daysInMonth()
  
  daysInMonth.value = []
  for (let i = 1; i <= days; i++) {
    daysInMonth.value.push({
      date: `${year}-${String(month + 1).padStart(2, '0')}-${String(i).padStart(2, '0')}`,
      day: i
    })
  }
}

// 处理可视化数据
function processVisualData(schedulings) {
  loadAllShifts().then(() => {
    // 先获取所有员工信息
    listEmployees({ pageSize: 10000 }).then(employeeResponse => { // 增大pageSize以确保获取所有员工
      // 获取所有员工的用户信息
      const userIds = employeeResponse.rows.map(employee => employee.userId);
      
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
        // 初始化所有员工数据
        const employeeScheduleMap = new Map()
        
        // 初始化所有员工，设置默认值
        employeeResponse.rows.forEach(employee => {
          // 获取员工详细用户信息
          const user = userInfoCache.value.get(employee.userId);
          employeeScheduleMap.set(employee.userId, {
            userId: employee.userId,
            userName: user ? user.userName : '未知用户',
            nickName: user ? user.nickName : '',
          })
          
          // 为每一天初始化空的排班信息
          daysInMonth.value.forEach(day => {
            employeeScheduleMap.get(employee.userId)[`day${day.day}`] = null
          })
        })
        
        // 填充实际的排班数据
        schedulings.forEach(item => {
          if (!employeeScheduleMap.has(item.userId)) {
            // 如果在员工列表中不存在该员工，则创建条目
            const user = userInfoCache.value.get(item.userId);
            employeeScheduleMap.set(item.userId, {
              userId: item.userId,
              userName: user ? user.userName : '未知用户',
              nickName: user ? user.nickName : '',
            })
            
            daysInMonth.value.forEach(day => {
              employeeScheduleMap.get(item.userId)[`day${day.day}`] = null
            })
          }
          
          // 设置该员工在特定日期的排班信息
          const day = dayjs(item.scheduleDate).date()
          employeeScheduleMap.get(item.userId)[`day${day}`] = {
            id: item.id, // 保存排班记录ID
            shiftId: item.shiftId,
            status: item.status,
            remarks: item.remarks
          }
        })
        
        // 转换为数组并更新视图
        visualSchedulingData.value = Array.from(employeeScheduleMap.values())
        visualLoading.value = false
      });
    }).catch(() => {
      visualLoading.value = false
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

  // 获取班次显示文本
  function getShiftDisplay(shiftInfo) {
    if (!shiftInfo) return ''
    
    const shift = shiftMap.value.get(shiftInfo.shiftId)
    if (shift) {
      return shift.shiftName
    }
    return '未知班次'
  }

  // 获取班次详细信息用于提示框
  function getShiftTooltip(shiftInfo) {
    if (!shiftInfo) return ''
    
    const shift = shiftMap.value.get(shiftInfo.shiftId)
    if (shift) {
      return `班次: ${shift.shiftName}\n状态: ${getStatusText(shiftInfo.status)}\n备注: ${shiftInfo.remarks || '无'}`
    }
    return '未知班次'
  }

  // 获取状态文本
  function getStatusText(status) {
    const statusDict = layout_status.value.find(item => item.value == status)
    return statusDict ? statusDict.label : '未知状态'
  }

  // 获取班次样式类
  function getShiftClass(shiftInfo) {
    if (!shiftInfo) return ''
    
    // 根据状态返回不同样式
    switch (shiftInfo.status) {
      case '0': // 已确认
        return 'status-completed'
      case '1': // 已取消
        return 'status-cancelled'
      case '2': // 待确认
        return 'status-pending'
      default:
        return ''
    }
  }

  // 确认班次
  function confirmShift(row, day) {
    // 使用可视化数据中的ID直接更新，避免重复查询
    const dayKey = 'day' + day.day
    const scheduleInfo = row[dayKey]
    
    if (scheduleInfo && scheduleInfo.id) {
      // 直接使用已有的排班记录ID进行更新
      const scheduleRecord = {
        id: scheduleInfo.id,
        userId: row.userId,
        shiftId: scheduleInfo.shiftId,
        scheduleDate: day.date,
        status: '0', // 设置为已确认状态
        remarks: scheduleInfo.remarks || null
      }
      
      updateScheduling(scheduleRecord).then(updateResponse => {
        proxy.$modal.msgSuccess("班次确认成功")
        // 直接更新本地数据以立即反映变化
        // 更新可视化数据中的对应项
        const updatedData = [...visualSchedulingData.value]
        const userRow = updatedData.find(item => item.userId === row.userId)
        if (userRow) {
          const dayKey = 'day' + day.day
          if (userRow[dayKey]) {
            userRow[dayKey].status = '0'
            visualSchedulingData.value = updatedData
          }
        }
        // 刷新可视化数据以确保一致性，使用更长的延迟确保后端处理完成
        if (visualMode.value) {
          setTimeout(() => {
            loadVisualData() // 完全刷新可视化数据，确保状态变更持久化
          }, 1000) // 延迟1秒再刷新，确保后端有足够时间处理更新
        }
        // 同时刷新列表数据
        setTimeout(() => {
          getList()
        }, 1000) // 延迟1秒再刷新列表数据
      }).catch(error => {
        proxy.$modal.msgError("班次确认失败: " + error.msg)
        // 刷新数据以恢复到正确状态
        if (visualMode.value) {
          loadVisualData()
        }
        getList()
      })
    } else {
      // 如果没有找到ID，回退到原来的查询方式
      listScheduling({
        userId: row.userId,
        scheduleDate: day.date,
        pageSize: 1
      }).then(response => {
        if (response.rows && response.rows.length > 0) {
          const scheduleRecord = { ...response.rows[0] } // 复制对象以避免直接修改响应数据
          // 更新排班状态为已确认（状态'0'表示已确认）
          scheduleRecord.status = '0'
          updateScheduling(scheduleRecord).then(updateResponse => {
            proxy.$modal.msgSuccess("班次确认成功")
            // 直接更新本地数据以立即反映变化
            // 更新可视化数据中的对应项
            const updatedData = [...visualSchedulingData.value]
            const userRow = updatedData.find(item => item.userId === row.userId)
            if (userRow) {
              const dayKey = 'day' + day.day
              if (userRow[dayKey]) {
                userRow[dayKey].status = '0'
                visualSchedulingData.value = updatedData
              }
            }
            // 刷新可视化数据以确保一致性，使用更长的延迟确保后端处理完成
            if (visualMode.value) {
              setTimeout(() => {
                loadVisualData() // 完全刷新可视化数据，确保状态变更持久化
              }, 1000) // 延迟1秒再刷新，确保后端有足够时间处理更新
            }
            // 同时刷新列表数据
            setTimeout(() => {
              getList()
            }, 1000) // 延迟1秒再刷新列表数据
          }).catch(error => {
            proxy.$modal.msgError("班次确认失败: " + error.msg)
            // 刷新数据以恢复到正确状态
            if (visualMode.value) {
              loadVisualData()
            }
            getList()
          })
        } else {
          proxy.$modal.msgError("未找到对应的排班记录")
        }
      }).catch(error => {
        proxy.$modal.msgError("获取排班记录失败: " + error.msg)
      })
    }
  }

  // 处理单元格点击事件
  function handleCellClick(row, day, scheduleInfo) {
    if (scheduleInfo) {
      // 查看现有排班（只读模式）
      viewVisualSchedule(row.userId, day.date, scheduleInfo)
    } else {
      // 新增排班
      addVisualSchedule(row.userId, day.date)
    }
  }

  // 编辑可视化排班
  function editVisualSchedule(userId, date, scheduleInfo) {
    // 根据用户ID和日期查找完整的排班记录
    listScheduling({
      userId: userId,
      scheduleDate: date,
      pageSize: 1  // 只需要一条记录
    }).then(response => {
      if (response.rows && response.rows.length > 0) {
        handleUpdate(response.rows[0])
        
        // 确保用户信息已加载
        if (userId && !userInfoCache.value.has(userId)) {
          getUser(userId).then(userRes => {
            userInfoCache.value.set(userId, userRes.data);
          }).catch(() => {
            userInfoCache.value.set(userId, { 
              userId: userId, 
              userName: '未知用户', 
              nickName: '' 
            });
          });
        }
      }
    }).catch(() => {
      proxy.$modal.msgError("获取排班信息失败")
    })
  }
  
  // 查看可视化排班（只读模式）
  function viewVisualSchedule(userId, date, scheduleInfo) {
    // 根据用户ID和日期查找完整的排班记录
    listScheduling({
      userId: userId,
      scheduleDate: date,
      pageSize: 1
    }).then(response => {
      if (response.rows && response.rows.length > 0) {
        handleView(response.rows[0])
      }
    }).catch(() => {
      proxy.$modal.msgError("获取排班信息失败")
    })
  }

  // 新增可视化排班
  function addVisualSchedule(userId, date) {
    reset()
    form.value.userId = userId
    form.value.scheduleDate = date
    open.value = true
    title.value = "添加排班情况"
    
    // 确保用户信息已加载
    if (userId && !userInfoCache.value.has(userId)) {
      getUser(userId).then(userRes => {
        userInfoCache.value.set(userId, userRes.data);
      }).catch(() => {
        userInfoCache.value.set(userId, { 
          userId: userId, 
          userName: '未知用户', 
          nickName: '' 
        });
      });
    }
  }

// 添加导出状态
const exportLoading = ref(false)

// 定时刷新相关
let refreshTimer = null

// 开启定时刷新
function startAutoRefresh() {
  // 清除已有的定时器
  if (refreshTimer) {
    clearInterval(refreshTimer)
  }
  
  // 每分钟刷新一次数据（当在可视化模式时）
  refreshTimer = setInterval(() => {
    if (visualMode.value) {
      loadVisualData()
    }
  }, 60000) // 每分钟刷新一次
}

// 停止定时刷新
function stopAutoRefresh() {
  if (refreshTimer) {
    clearInterval(refreshTimer)
    refreshTimer = null
  }
}

// 导出可视化视图为图片
function exportVisualAsImage() {
  exportLoading.value = true
  
  nextTick(() => {
    const element = document.querySelector('.visual-table')
    if (!element) {
      proxy.$modal.msgError("未找到可视化表格元素")
      exportLoading.value = false
      return
    }
    
    // 使用 html2canvas 库将 DOM 元素转换为图片
    import('html2canvas').then(html2canvas => {
      html2canvas.default(element, {
        backgroundColor: '#ffffff',
        scale: 2, // 提高图片质量
        useCORS: true,
        logging: false
      }).then(canvas => {
        const imageLink = document.createElement('a')
        const imageData = canvas.toDataURL('image/png')
        imageLink.download = `排班表_${visualMonth.value}.png`
        imageLink.href = imageData
        imageLink.click()
        
        exportLoading.value = false
        proxy.$modal.msgSuccess("导出成功")
      }).catch(() => {
        proxy.$modal.msgError("导出失败")
        exportLoading.value = false
      })
    }).catch(() => {
      proxy.$modal.msgError("导出功能加载失败")
      exportLoading.value = false
    })
  })
}

// 组件卸载时清理定时器
onUnmounted(() => {
  stopAutoRefresh()
})

// 组件挂载时获取当前用户信息
onMounted(() => {
  getList()
  // 从store获取当前用户信息
  const userStore = useUserStore()
  currentUserId.value = userStore.id
  console.log('Current user ID:', currentUserId.value) // 调试信息
})

getList()
</script>

<style scoped>
.visual-scheduling-container {
  margin-top: 15px;
}

.visual-header {
  margin-bottom: 15px;
}

.header-controls {
  display: flex;
  align-items: center;
  gap: 15px;
}

.employee-info .nick-name {
  font-size: 12px;
  color: #999;
}

.shift-cell {
  padding: 4px;
  border-radius: 4px;
  font-size: 12px;
}

.status-pending {
  background-color: #fff7e6;
  color: #f5222d;
}

.status-completed {
  background-color: #f6ffed;
  color: #52c41a;
}

.status-cancelled {
  background-color: #bebebe;
  color: #f0f0f0;
}

.visual-table :deep(.el-table__body-wrapper) {
  min-height: 400px;
}

.shift-cell.editable-cell {
  cursor: pointer;
  transition: all 0.3s;
}

.shift-cell.editable-cell:hover {
  transform: scale(1.05);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.add-symbol {
  font-size: 20px;
  color: #cccccc;
  font-weight: bold;
}

.add-symbol:hover {
  color: #409eff;
}

.visual-table :deep(.el-table__body-wrapper) {
  min-height: 400px;
}

/* 为导出优化表格样式 */
.visual-table.export-optimized :deep(.shift-cell) {
  min-height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.visual-table.export-optimized :deep(.el-table__cell) {
  padding: 2px 0;
}

.shift-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.shift-name {
  margin-bottom: 4px;
}

.confirm-btn {
  background-color: #409eff;
  color: white;
  font-size: 10px;
  padding: 2px 6px;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 2px;
}

.confirm-btn:hover {
  background-color: #66b1ff;
}

.confirmed-text {
  color: #67c23a;
  font-size: 10px;
  margin-top: 2px;
}
</style>
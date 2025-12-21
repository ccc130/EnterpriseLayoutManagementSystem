<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户编号" prop="userId">
        <el-autocomplete
          v-model="queryParams.userId"
          placeholder="请输入用户编号"
          clearable
          :fetch-suggestions="queryUsers"
          :trigger-on-focus="false"
          @select="handleUserSelect"
          @keyup.enter="handleQuery"
        >
          <template #default="{ item }">
            <div>{{ item.userName }} ({{ item.nickName }})</div>
          </template>
        </el-autocomplete>
      </el-form-item>
      <el-form-item label="入职日期" style="width: 308px">
        <el-date-picker
          v-model="daterangeHireDate"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="每周工作时长" prop="workHoursPerWeek">
        <el-input
          v-model="queryParams.workHoursPerWeek"
          placeholder="请输入每周工作时长(小时)"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最大连续工作天数" prop="maxConsecutiveDays">
        <el-input
          v-model="queryParams.maxConsecutiveDays"
          placeholder="请输入最大连续工作天数"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="周末出勤偏好" prop="weekendPreference">
        <el-select v-model="queryParams.weekendPreference" placeholder="请选择周末出勤偏好" clearable style="width: 200px;">
          <el-option
            v-for="dict in layout_weekend"
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
    <br />

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['employees:employees:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['employees:employees:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['employees:employees:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['employees:employees:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="employeesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户编号" align="center" prop="userId" />
      <el-table-column label="用户信息" align="center" prop="userId">
        <template #default="scope">
          <span>{{ getUserDisplayName(scope.row.userId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="员工照片" align="center" prop="picture" width="100">
        <template #default="scope">
          <image-preview :src="scope.row.picture" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="部门" align="center" prop="departmentId">
        <template #default="scope">
          <span>{{ getDeptName(scope.row.departmentId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="入职日期" align="center" prop="hireDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.hireDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="每周工作时长(小时)" align="center" prop="workHoursPerWeek"/>
      <el-table-column label="最大连续工作天数" align="center" prop="maxConsecutiveDays" />
      <el-table-column label="周末出勤偏好" align="center" prop="weekendPreference" />
      <el-table-column label="备注" align="center" prop="skills" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['employees:employees:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['employees:employees:remove']">删除</el-button>
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

    <!-- 添加或修改员工信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="employeesRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名称" prop="userId">
          <el-select 
            v-model="form.userId" 
            placeholder="请输入用户名称"
            clearable
            filterable
            remote
            :remote-method="searchUsers"
            :loading="userLoading"
            reserve-keyword
            @change="handleUserChange"
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
        <el-form-item label="员工照片" prop="picture">
          <image-upload v-model="form.picture" :limit="1"/>
        </el-form-item>
        <el-form-item label="部门" prop="departmentId">
          <el-input :value="getDeptName(form.departmentId)" placeholder="请输入部门" readonly />
        </el-form-item>
        <el-form-item label="入职日期" prop="hireDate">
          <el-date-picker clearable
            v-model="form.hireDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择入职日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="每周工作时长" prop="workHoursPerWeek">
          <el-input v-model="form.workHoursPerWeek" placeholder="请输入每周工作时长(小时)" />
        </el-form-item>
        <el-form-item label="最大连续工作天数" prop="maxConsecutiveDays">
          <el-input v-model="form.maxConsecutiveDays" placeholder="请输入最大连续工作天数" />
        </el-form-item>
        <el-form-item label="周末出勤偏好" prop="weekendPreference">
          <el-select v-model="form.weekendPreference" placeholder="请选择周末出勤偏好">
            <el-option
              v-for="dict in layout_weekend"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="skills">
          <el-input v-model="form.skills" placeholder="请输入备注" />
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

<script setup name="Employees">
import { listEmployees, getEmployees, delEmployees, addEmployees, updateEmployees } from "@/api/employees/employees"
import { listUser, getUser } from "@/api/system/user"
import { listDept } from "@/api/system/dept"
  
const { proxy } = getCurrentInstance()
const { layout_weekend } = proxy.useDict('layout_weekend')

const employeesList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const daterangeHireDate = ref([])
const userLoading = ref(false)
const userList = ref([])
const userInfoCache = ref(new Map())
const deptMap = ref(new Map())

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userId: null,
    picture: null,
    departmentId: null,
    hireDate: null,
    workHoursPerWeek: null,
    maxConsecutiveDays: null,
    weekendPreference: null,
    skills: null
  },
  rules: {
    userId: [
      { required: true, message: "用户名称不能为空", trigger: "blur" }
    ],
    hireDate: [
      { required: true, message: "入职日期不能为空", trigger: "blur" }
    ],
    workHoursPerWeek: [
      { type: "number", message: "每周工作时长必须为数字值", trigger: "blur" }
      ,
      { type: "number", min: 0, message: "每周工作时长不能小于0小时", trigger: "blur" }
    ]
    ,
    maxConsecutiveDays: [
      { type: "number", message: "最大连续工作天数必须为数字值", trigger: "blur" }
      ,
      { type: "number", min: 0, message: "最大连续工作天数不能小于0天", trigger: "blur" }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询员工信息列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeHireDate && '' != daterangeHireDate) {
    queryParams.value.params["beginHireDate"] = daterangeHireDate.value[0]
    queryParams.value.params["endHireDate"] = daterangeHireDate.value[1]
  }
  listEmployees(queryParams.value).then(response => {
    employeesList.value = response.rows
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
    userId: null,
    picture: null,
    departmentId: null,
    hireDate: null,
    workHoursPerWeek: null,
    maxConsecutiveDays: null,
    weekendPreference: null,
    skills: null    
  }
  userList.value = []
  proxy.resetForm("employeesRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeHireDate.value = []
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
  getAllUsers()
  open.value = true
  title.value = "添加员工信息"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  getAllUsers()
  const _id = row.id || ids.value
  getEmployees(_id).then(response => {
    form.value = response.data
    if (response.data.userId) {
      nextTick(() => {
        fetchUserDetailsAndFill(response.data.userId);
      });
    }
    open.value = true
    title.value = "修改员工信息"
  })
  const userId = row.userId || ids.value
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["employeesRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateEmployees(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addEmployees(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除员工信息编号为"' + row.userId + '"的数据项？').then(function() {
    return delEmployees(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('employees/employees/export', {
    ...queryParams.value
  }, `employees_${new Date().getTime()}.xlsx`)
}

/**获取所有用户列表*/
function getAllUsers() {
  listUser({
    pageSize: 1000  // 获取足够多的用户数据
  }).then(response => {
    userList.value = response.rows
  })
}

/** 获取用户显示名称 */
function getUserDisplayName(userId) {
  if (userInfoCache.value.has(userId)) {
    const user = userInfoCache.value.get(userId)
    return `${user.userName}(${user.nickName})`
  }
  
  if (userId) {
    getUser(userId).then(response => {
      const user = response.data
      userInfoCache.value.set(userId, user)
      employeesList.value = [...employeesList.value]
    }).catch(() => {
      return userId
    })
  }
  return userId
}

// 查询用户建议
function queryUsers(queryString, cb) {
  if (!queryString) {
    cb([]);
    return;
  }
  
  listUser({
    userName: queryString,
    pageSize: 20
  }).then(response => {
    const users = response.rows.map(user => ({
      value: user.userId,
      userName: user.userName,
      nickName: user.nickName
    }));
    cb(users);
  }).catch(() => {
    cb([]);
  });
}

// 处理用户选择
function handleUserSelect(item) {
  queryParams.value.userId = item.value;
}
/** 搜索用户 */
function searchUsers(query) {
  userLoading.value = true;
  listUser({
    userName: query,
    pageSize: 100
  }).then(response => {
    userList.value = response.rows;
    userLoading.value = false;
  }).catch(() => {
    userLoading.value = false;
  });
}

/** 获取用户详情并填充表单 */
function fetchUserDetailsAndFill(userId) {
  if (!userId) return;
  getUser(userId).then(response => {
    const user = response.data;
    form.value.departmentId = user.deptId || '';

    const index = userList.value.findIndex(item => item.userId === userId);
    if (index !== -1) {
      userList.value[index] = user;
    }
  }).catch(error => {
    console.error("获取用户详细信息失败:", error);
  });
}

// 处理用户选择变更，自动填充部门
function handleUserChange(userId) {
  if (userId) {
    form.value.departmentId = '';
    nextTick(() => {
      fetchUserDetailsAndFill(userId);
    });
  } else {
    form.value.departmentId = '';
   }
}

// 获取部门名称
function getDeptName(deptId) {
  return deptMap.value.get(deptId) || deptId;
}

// 获取部门信息
function getDeptInfo() {
  listDept().then(response => {
    response.data.forEach(dept => {
      deptMap.value.set(dept.deptId, dept.deptName);
    });
  });
}

// 在组件初始化时获取部门信息
onMounted(() => {
  getDeptInfo();
  getList();
});

getList()
</script>

<template>
  <div class="app-container">
    <div class="block">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="search_name" size="mini" placeholder="请输入角色名称" />
        </el-col>
        <el-col :span="6">
          <el-button type="success" size="mini" icon="el-icon-search" @click.native="search">{{ $t('button.search') }}</el-button>
          <el-button type="primary" size="mini" icon="el-icon-refresh" @click.native="reset">{{ $t('button.reset') }}</el-button>
        </el-col>
      </el-row>
      <br>
      <el-row>
        <el-col :span="24">
          <el-button type="success" size="mini" icon="el-icon-plus" @click.native="add">{{ $t('button.add') }}</el-button>
          <el-button type="primary" size="mini" icon="el-icon-edit" @click.native="edit">{{ $t('button.edit') }}</el-button>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click.native="remove">{{ $t('button.delete') }}</el-button>
          <el-button type="primary" size="mini" icon="el-icon-setting" @click.native="openPermissions">权限配置</el-button>
        </el-col>
      </el-row>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column label="序号" min-width="20px" align="center">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="名称">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="编码">
        <template slot-scope="scope">
          {{ scope.row.tips }}
        </template>
      </el-table-column>
      <el-table-column label="所在部门">
        <template slot-scope="scope">
          {{ scope.row.deptName }}
        </template>
      </el-table-column>
      <!-- <el-table-column label="上级角色">
        <template slot-scope="scope">
          {{ scope.row.pName }}
        </template>
      </el-table-column> -->

    </el-table>

    <!-- <el-pagination
      background
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 50, 100,500]"
      :page-size="listQuery.limit"
      :total="total"
      @size-change="changeSize"
      @current-change="fetchPage"
      @prev-click="fetchPrev"
      @next-click="fetchNext"
    /> -->

    <el-dialog
      :title="formTitle"
      :visible.sync="formVisible"
      width="50%"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" style="margin-left:30px;margin-right:30px">
        <el-row>
          <!-- <el-col :span="12"> -->
            <el-form-item label="角色编码" prop="tips">
              <el-input v-model="form.tips" minlength="1" />
            </el-form-item>
          <!-- </el-col> -->
          <!-- <el-col :span="12"> -->
            <el-form-item label="角色名称" prop="name">
              <el-input v-model="form.name" minlength="1" />
            </el-form-item>
          <!-- </el-col> -->

          <!-- <el-col :span="12">
            <el-form-item label="上级角色">
              <el-input
                v-model="form.pName"
                placeholder="请选择上级角色"
                readonly="readonly"
                @click.native="roleTree.show = !roleTree.show"
              />
              <el-tree
                v-if="roleTree.show"
                empty-text="暂无数据"
                :expand-on-click-node="false"
                :data="list"
                :props="roleTree.defaultProps"
                class="input-tree"
                @node-click="handleRoleNodeClick"
              /> -->

            <!-- </el-form-item> -->
          <!-- </el-col> -->
          <!-- <el-col :span="12">
            <el-form-item label="排序">
              <el-input v-model="form.num" type="number" />
            </el-form-item>
          </el-col> -->

          <!-- <el-col :span="12"> -->
            <el-form-item label="所在部门">
              <el-input
                v-model="form.deptName"
                placeholder="请选择所在部门"
                readonly="readonly"
                @click.native="deptTree.show = !deptTree.show"
              />
              <el-tree
                v-if="deptTree.show"
                empty-text="暂无数据"
                :expand-on-click-node="false"
                :data="deptList"
                :props="deptTree.defaultProps"
                class="input-tree"
                @node-click="handleDeptNodeClick"
              />

            </el-form-item>
          <!-- </el-col> -->

        </el-row>
        <el-form-item>
          <div style="float:right">
            <el-button type="primary" @click="save">{{ $t('button.submit') }}</el-button>
            <el-button @click.native="formVisible = false">{{ $t('button.cancel') }}</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog
      title="权限配置"
      :visible.sync="permissonVisible"
      width="25%"
    >
      <el-form>
        <el-row>
          <el-col :span="12">
            <el-tree
              ref="permissonTree"
              :data="permissons"
              show-checkbox
              node-key="id"
              :default-checked-keys="checkedPermissionKeys"
              :props="defaultProps"
            />

          </el-col>
        </el-row>
        <el-form-item>
          <div  style="text-align:center;padding-top:20px">
            <el-button type="primary" @click="savePermissions" :loading="buttonLoad">{{ $t('button.submit') }}</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script src="./role.js"></script>
<style rel="stylesheet/scss" lang="scss">
  @import "src/styles/common.scss";
  .el-tree-node__label {
    font-size: 14px;
    margin-left: 8px;
}
</style>

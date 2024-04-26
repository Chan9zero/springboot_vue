<template>
  <div>
  <div>
    <el-input style="width: 200px" placeholder="查询用户名" v-model="username"></el-input>
    <el-input style="width: 200px; margin:0 5px"placeholder="查询姓名" v-model="name">></el-input>
    <el-button type="primary" @click="load(1)">查询</el-button>
    <el-button type="info" @click="reset">重置</el-button>
  </div>
  <div style="margin: 10px 0">
    <el-button type="primary" plain @click="handleAdd">新增</el-button>
    <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    <el-button type="info" plain @click="exportData">批量导出</el-button>
    <el-upload :action="$baseUrl+'/user/import'" :headers="{token: user.token}"
               :on-success="handleImport" style="display: inline-block; margin-left: 10px"
               :show-file-list="false">
      <el-button type="primary" plain>批量导入</el-button>
    </el-upload>
  </div>

  <el-table :data="tableData" stripe  :header-cell-style="{backgroundColor: 'aliceblue', color: '#666'}"
            @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55" align="center"></el-table-column>
    <el-table-column label="ID" prop="id" width="70" align="center"></el-table-column>
    <el-table-column label="用户名" prop="username"></el-table-column>
    <el-table-column label="姓名" prop="name"></el-table-column>
    <el-table-column label="手机号" prop="phone"></el-table-column>
    <el-table-column label="邮箱" prop="email"></el-table-column>
    <el-table-column label="地址" prop="address"></el-table-column>
    <el-table-column label="头像">
      <template v-slot="scope">
        <div style="display: flex; align-items: center">
          <el-image style="width: 50px; height: 50px; border-radius: 50%" v-if="scope.row.avatar"
                    :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]">
          </el-image>
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="role" label="角色"></el-table-column>
    <el-table-column label="操作" align="center" width="180">
      <template v-slot="scope">
        <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
        <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[100, 200, 300, 400]"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="收货地址" :visible.sync="formVisible" width="30%">
      <el-form :model="form" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input type="textarea" v-model="form.address" placeholder="地址"></el-input>
        </el-form-item>
        <el-form-item label="权限" prop="role">
          <el-radio-group v-model="form.role">
            <el-radio label="管理员"></el-radio>
            <el-radio label="用户"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl+'/file/upload'"
              :headers=" { token: user.token }"
              :file-list="form.avatar ? [form.avatar] : []"
              list-type="picture"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


</div>
</template>

<script>
export default {
  name: "User",
  data(){
    return{
      tableData: [], //所有的数据
      pageNum: 1, //当前页码
      pageSize: 5, //每页显示的个数
      username: '',
      name: '',
      total: 0,
      formVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      rules: {
        username:[
          {required: true, message: '请输入账号', trigger: 'blur'}
        ]
      },
      ids: []
    }
  },
  created() {
    this.load()
  },
  methods:{
    handleImport(res, file, fileList){
      if (res.code === '200'){
        this.$message.success("操作成功")
        this.load(1)
      } else {
        this.$message.error(res.msg)
      }
    },
    exportData(){  //批量导出数据
      if(!this.ids.length){ //如果没有选中则导出全部或者根据搜索导出
        window.open(this.$baseUrl + '/user/export?token='+ this.user.token
        + "&username=" + this.username + "&name=" + this.name)
      } else {
        let idStr = this.ids.join(',')
        window.open(this.$baseUrl + '/user/export?token='+ this.user.token
            + "&ids=" + idStr)
      }
    },
    handleSelectionChange(rows){  //当前选中的所有行信息
      this.ids = rows.map(v => v.id)
    },
    delBatch(){
      if(!this.ids.length){
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('是否确定批量删除？', '确认删除', {type:"warning"}).then(response =>{
        this.$request.delete('/user/delete/batch', {data: this.ids}).then(res =>{
          if(res.code === '200'){
            this.$message.success('操作成功')
            this.load(1)
          } else{
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    del(id){
      this.$confirm('是否确认删除？', '确认删除', {type:"warning"}).then(response =>{
        this.$request.delete('/user/delete/' + id).then(res =>{
          if(res.code === '200'){
            this.$message.success('操作成功')
            this.load(1)
          } else{
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    handleEdit(row){  //编辑数据
      this.form = JSON.parse(JSON.stringify(row))  //给form对象赋值，深拷贝
      this.formVisible = true //打开弹窗
    },
    handleAdd(){  //新增数据
      this.form = { role: '用户' }  //点击新增时清空数据
      this.formVisible=true //打开弹窗
    },
    save(){ //保存按钮触发的逻辑 他会触发新增或者更新
      this.$refs.formRef.validate((valid)=> {
        if(valid){
          this.$request({
            url: this.form.id ? 'user/update': 'user/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if(res.code === '200'){ //表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.formVisible = false
            } else{
              this.$message.error(res.msg) //失败
            }
          })

        }

      })
    },
    reset(){
      this.name=''
      this.username=''
      this.load(1)
    },
    load(pageNum){  //分页查询
      if(pageNum){
        this.pageNum=pageNum
      }
      this.$request.get('/user/selectByPage', {
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          name: this.name
        }
      }).then(res =>{
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    handleCurrentChange(pageNum){
      this.pageNum = pageNum
      this.load()
    },
    handleAvatarSuccess(response, file, fileList){
      this.form.avatar = response.data
    },
  }
}
</script>

<style scoped>

</style>
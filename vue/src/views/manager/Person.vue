<template>
  <div>
    <el-card style="width: 50%">
      <el-form :model="user" label-width="80px" style="padding-right: 20px">
        <div style="margin: 10px; text-align: center" >
          <el-upload
            class="avatar-uploader"
            :action="$baseUrl+'/file/upload'"
            :headers=" { token: user.token }"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            >
            <img v-if="user.avatar" :src="user.avatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>

        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" placeholder="用户名" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input type="textarea" v-model="user.address" placeholder="地址"></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button size="small" type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Person",
  data(){
    return {
      user: JSON.parse(localStorage.getItem('honey-user') || '{}')
    }
  },
  methods:{
    update() {
      this.$request.put('/user/update', this.user).then(res =>{
        if(res.code === '200'){
          //成功更新
          this.$message.success('保存成功')
          localStorage.setItem('honey-user', JSON.stringify(this.user))
          this.$emit('update:user', this.user) //触发父级的数据更新
        } else{
          this.$message.error(res.msg)
        }
      })
    },
    handleAvatarSuccess(response, file, fileList){
      this.user.avatar = response.data
    }
  }
}
</script>

<style scoped>
/deep/.el-form-item__label{
  font-weight: bold;
}
/deep/.el-upload{
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  border-radius: 50%;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
  border-radius: 50%;
}

</style>
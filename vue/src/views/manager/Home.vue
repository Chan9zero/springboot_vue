<template>
<div>
  <div style="box-shadow: 0 0 10px rgba(0,0,0,.1); padding: 10px 20px; border-radius: 5px; border-radius: 5px; margin-bottom: 10px">
    你好，{{ user.name }}！
  </div>
  <div style="display: flex">
    <el-card style="width: 100%; margin-right: 10px">
      <div slot="header" class="clearfix">
        <span>毕设2024</span>
      </div>
      <div>
        2024毕设正式开始了!
        <div style="margin-top: 20px">
          <div style="margin: 10px 0"><strong>主题色</strong></div>
          <el-button type="primary">按钮</el-button>
          <el-button type="success">按钮</el-button>
          <el-button type="warning">按钮</el-button>
          <el-button type="danger">按钮</el-button>
          <el-button type="info">按钮</el-button>
        </div>
      </div>
    </el-card>
<!--    <el-card style="width: 70%">-->
<!--      <div slot="header" class="clearfix">-->
<!--        <span>学习毕设2024</span>-->
<!--      </div>-->
<!--      <div>-->

<!--          <el-table-column label="文件上传">-->
<!--            <template v-slot="scope">-->
<!--              <el-upload-->
<!--                  action="http://localhost:9090/file/upload"-->
<!--                  :headers="{token: user.token}"-->
<!--                  :show-file-list="false"-->
<!--                  :on-success="(res, file, fileList, row) => handleTableFileUpload(res, file, fileList, scope.row)"-->
<!--              >-->
<!--                <el-button size="mini" type="primary">点击上传</el-button>-->
<!--              </el-upload>-->
<!--            </template>-->
<!--          </el-table-column>-->

<!--          <el-table-column label="文件上传">-->
<!--            <template v-slot="scope">-->
<!--              <el-image v-if="scope.row.avatar" :src="scope.row.avatar" style="width: 50px; height: 50px"></el-image>-->
<!--              <div><el-button @click="preview(scope.row.avatar)">预览</el-button></div>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--      </div>-->
<!--    </el-card>-->
  </div>
  <!--        主体区域-->

<!--  <div style="display: flex; margin: 10px 0">-->
<!--    <el-card style="width: 50%; margin-right: 10px">-->
<!--      <div slot="header" class="clearfix">-->
<!--        <span>文件上传下载</span>-->
<!--      </div>-->
<!--      <div>-->
<!--        <el-upload-->
<!--            action="http://localhost:9090/file/upload"-->
<!--            :headers="{token: user.token}"-->
<!--            :file-list="fileList"-->
<!--            list-type="picture"-->
<!--            :on-success="handleFileUpload"-->
<!--        >-->
<!--          <el-button size="small" type="primary">单文件上传</el-button>-->
<!--          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
<!--        </el-upload>-->

<!--        <div style="margin: 10px 0" >-->
<!--          <el-input v-model="url"></el-input>-->
<!--          <el-button size="small" type="primary">下载文件</el-button>-->
<!--        </div>-->
<!--      </div>-->

<!--      <div style="margin: 10px 0">-->
<!--        <el-upload-->
<!--            action="http://localhost:9090/file/upload"-->
<!--            :headers="{token: user.token}"-->
<!--            :on-success="handleMultipleFileUpload"-->
<!--            multiple-->
<!--        >-->
<!--          <el-button size="small" type="success">多文件上传</el-button>-->
<!--        </el-upload>-->

<!--        <el-button type="primary" style="margin: 10px 0" @click="showUrls">显示上传的数据</el-button>-->

<!--      </div>-->
<!--    </el-card>-->
<!--  </div>-->
</div>
</template>

<script>
export default {
  name: "Home",
  data(){
    return {
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
    }
  }

}
</script>

<style scoped>

</style>
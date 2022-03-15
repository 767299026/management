<template>
  <div id="login" class="login_container">
    <img src="../static/fracture.webp" alt="facture.webp">
    <div class="login_box">
      <!--头像区域-->
      <div class="avatar_box">
        <img src="../static/avatar.jpg" alt="avatar.jpg">
      </div>
      <!--表单区域-->
      <div>
        <el-form ref="loginFormRef" :model="loginForm" :rules="loginFormRules" label-width="0px" class="login_form">
          <!--用户名-->
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" prefix-icon="el-icon-user-solid"></el-input>
          </el-form-item>
          <!--密码-->
          <el-form-item prop="password">
            <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" type="password"></el-input>
          </el-form-item>
          <!--按钮-->
          <el-form-item class="btns">
            <el-button type="primary" @click="login">登录</el-button>
            <el-button type="info" @click="resetLoginForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      loginForm: {//登录表单数据
        username: '',
        password: ''
      },
      loginFormRules: {//表单验证规则
        username: [
          {
            required: true,
            message: "请输入正确的用户名",
            trigger: "blur"
          },
          {
            min: 3,
            max: 12,
            message: "长度在3-12个字符",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            message: "请输入正确的密码",
            trigger: "blur"
          },
          {
            min: 6,
            max: 15,
            message: "长度在6-15个字符",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    resetLoginForm() {
      this.$refs.loginFormRef.resetFields();
    },
    login() {
      this.$refs.loginFormRef.validate(valid =>{
        if (!valid) return;
        //this.$axios.post("/login",this.loginForm)
        this.$router.push("/home");
      });
    }
  }
}

</script>

<style lang = "less" scoped>

.login_container{
  //background-color: rgba(2, 180, 182, 0.73);
  height: 100%;
  img{
    width: 100%;
    height: 100%;
  }
}
.login_box{
  width: 450px;
  height: 300px;
  background-color: white;
  border-radius: 3px;//圆角
  position: absolute;//绝对定位
  left: 50%;
  top: 50%;
  transform: translate(-50%,-50%);
}
.avatar_box{
  height: 130px;
  width: 130px;
  border: 1px solid #eee;
  border-radius: 50%;
  padding: 10px;//边距
  box-shadow: 0 0 10px #ddd;
  position: absolute;
  left: 50%;
  transform: translate(-50%,-50%);
  background-color: #fff;
  img{
    width: 100%;
    height: 100%;
    border-radius: 50%;
  }
}
.login_form{
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}
.btns{
  display: flex;
  justify-content: flex-end;
}
</style>
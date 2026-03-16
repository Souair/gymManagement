<template>
  <div class="login-container">
    <div class="login-bg"></div>
    <div class="bg-mask"></div>

    <div class="login-card">
      <div class="card-header">
        <div class="logo-box">
          <img src="@/assets/logo/logo.png" style="width: 100%; height: 100%; border-radius: 12px;">
        </div>
        <h3 class="title">SMART FIT</h3>
        <p class="subtitle">智能健身房管理与预约系统</p>
      </div>

      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            type="text"
            auto-complete="off"
            placeholder="管理员账号 / Username"
          >
            <svg-icon slot="prefix" icon-class="user" class="input-icon" />
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            auto-complete="off"
            placeholder="密码 / Password"
            @keyup.enter.native="handleLogin"
          >
            <svg-icon slot="prefix" icon-class="password" class="input-icon" />
          </el-input>
        </el-form-item>

        <el-form-item prop="code" v-if="captchaEnabled">
          <div class="code-row">
            <el-input
              v-model="loginForm.code"
              auto-complete="off"
              placeholder="验证码"
              class="code-input"
              @keyup.enter.native="handleLogin"
            >
              <svg-icon slot="prefix" icon-class="validCode" class="input-icon" />
            </el-input>
            <div class="code-img-wrapper">
              <img :src="codeUrl" @click="getCode" class="code-img"/>
            </div>
          </div>
        </el-form-item>

        <div class="form-options">
          <el-checkbox v-model="loginForm.rememberMe">自动登录</el-checkbox>
        </div>

        <el-form-item>
          <el-button
            :loading="loading"
            type="primary"
            class="login-btn"
            @click.native.prevent="handleLogin"
          >
            <span v-if="!loading">登 录 系 统</span>
            <span v-else>正在验证...</span>
          </el-button>
        </el-form-item>

        <div class="register-bar" v-if="register">
          <span>还没有账号？</span>
          <router-link to="/register" class="reg-link">立即注册会员</router-link>
        </div>
      </el-form>
    </div>

    <div class="footer-text">
      {{ footerContent }}
    </div>
  </div>
</template>

<script>
// 逻辑代码完全保持不变
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from '@/utils/jsencrypt'
import defaultSettings from '@/settings'

export default {
  name: "Login",
  data() {
    return {
      title: process.env.VUE_APP_TITLE,
      footerContent: defaultSettings.footerContent,
      codeUrl: "",
      loginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      captchaEnabled: true,
      register: false,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCode()
    this.getCookie()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.loginForm.uuid = res.uuid
        }
      })
    },
    getCookie() {
      const username = Cookies.get("username")
      const password = Cookies.get("password")
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 })
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove("username")
            Cookies.remove("password")
            Cookies.remove('rememberMe')
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{})
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
// --------------------
// 变量定义
// --------------------
$bg-image: url("../assets/images/login-background.jpg"); // 背景图路径
$primary-color: #111827; // 主色调：接近纯黑的深灰，体现高端
$accent-color: #0056D2;  // 点缀色：科技蓝
$card-bg: rgba(255, 255, 255, 0.95); // 卡片背景：接近不透明的白
$input-bg: #F9FAFB;      // 输入框背景：极淡灰

.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100%;
  position: relative;
  overflow: hidden;
}

// 1. 背景层处理
.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: $bg-image;
  background-size: cover;
  background-position: center;
  z-index: 0;
  // 给背景加一点模糊，让前景卡片更突出
  filter: blur(8px);
  transform: scale(1.05); // 放大一点避免白边
}

.bg-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3); // 深色遮罩，让文字更清晰
  z-index: 1;
}

// 2. 悬浮卡片主体
.login-card {
  position: relative;
  z-index: 10;
  width: 440px;
  background: $card-bg;
  border-radius: 16px; // 大圆角
  padding: 50px 40px;

  // 核心：高级感的阴影
  box-shadow:
    0 20px 25px -5px rgba(0, 0, 0, 0.1),
    0 10px 10px -5px rgba(0, 0, 0, 0.04);

  // 入场动画
  animation: slideUp 0.6s cubic-bezier(0.16, 1, 0.3, 1);
}

.card-header {
  text-align: center;
  margin-bottom: 35px;

  .logo-box {
    width: 56px;
    height: 56px;
    border-radius: 12px;
    margin: 0 auto 15px;
    display: flex;
    justify-content: center;
    align-items: center;

    .logo-icon {
      font-size: 32px;
      color: #fff;
    }
  }

  .title {
    font-size: 26px;
    font-weight: 800;
    color: $primary-color;
    margin: 0;
    letter-spacing: 1px;
    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  }

  .subtitle {
    margin-top: 8px;
    color: #6B7280;
    font-size: 14px;
    font-weight: 500;
  }
}

// 3. 输入框深度定制
::v-deep .el-input {
  input {
    height: 50px; // 更高的输入框，便于点击
    background-color: $input-bg;
    border: 1px solid #E5E7EB;
    border-radius: 8px;
    padding-left: 45px;
    font-size: 15px;
    color: #374151;
    transition: all 0.2s;

    &::placeholder {
      color: #9CA3AF;
    }

    &:hover {
      border-color: #D1D5DB;
    }

    &:focus {
      background-color: #fff;
      border-color: $primary-color; // 聚焦变黑
      box-shadow: 0 0 0 3px rgba(0, 0, 0, 0.05);
    }
  }

  .el-input__prefix {
    left: 15px;
    display: flex;
    align-items: center;
  }
}

.input-icon {
  font-size: 18px;
  color: #6B7280;
}

// 验证码区域
.code-row {
  display: flex;
  gap: 15px;

  .code-input {
    flex: 2;
  }

  .code-img-wrapper {
    flex: 1;
    height: 50px;
    border: 1px solid #E5E7EB;
    border-radius: 8px;
    overflow: hidden;
    cursor: pointer;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
}

.form-options {
  display: flex;
  justify-content: space-between;
  margin-bottom: 25px;

  ::v-deep .el-checkbox__label {
    color: #6B7280;
  }
}

// 4. 按钮样式
.login-btn {
  width: 100%;
  height: 50px;
  background-color: $primary-color; // 纯黑按钮
  border: none;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  letter-spacing: 2px;
  transition: all 0.3s;

  &:hover {
    background-color: black;
    transform: translateY(-2px);
    box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  }
}

.register-bar {
  text-align: center;
  margin-top: 25px;
  font-size: 14px;
  color: #6B7280;

  .reg-link {
    color: $accent-color;
    font-weight: 600;
    margin-left: 5px;
    text-decoration: none;

    &:hover {
      text-decoration: underline;
    }
  }
}

.footer-text {
  position: absolute;
  bottom: 20px;
  color: rgba(255, 255, 255, 0.6); // 底部文字浅色
  font-size: 12px;
  z-index: 2;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(40px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>

<template>
  <div>
    <Card class="tl">
      <p slot="title">
        <Icon type="person"></Icon>
        个人信息
      </p>
      <div>
        <Form ref="userForm" :model="userForm" :label-width="100" label-position="right" :rules="inforValidate">
          <FormItem label="用户名：">
            <span>{{userForm.username}}</span>
          </FormItem>
          <FormItem label="登录密码：">
            <Button type="primary" ghost @click="showEditPassword">修改密码</Button>
          </FormItem>

          <FormItem label="用户手机：" prop="cellphone">
            <div style="display:inline-block;width:300px;">
              <Input v-model="userForm.cellphone"/>
            </div>
          </FormItem>
          <FormItem label="姓名：" prop="name">
            <div style="display:inline-block;width:300px;">
              <Input v-model="userForm.name"/>
              </div>
          </FormItem>
          <FormItem label="性别：" prop="gender">
            <RadioGroup v-model="userForm.gender">
              <Radio label="male" >男性</Radio>
              <Radio label="female">女性</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="生日：" prop="date">
            <DatePicker type="date" placeholder="选择生日" v-model="userForm.date"></DatePicker>
          </FormItem>
          <FormItem label="邮箱：" prop="email">
            <div style="display:inline-block;width:300px;">
              <Input v-model="userForm.email"/>
            </div>
          </FormItem>
          <div>
            <Button type="text" style="width: 100px;" @click="cancelEditUserInfor">取消</Button>
            <Button type="primary" style="width: 100px;" :loading="save_loading" @click="saveEdit">保存</Button>
          </div>
        </Form>
      </div>
    </Card>
    <Modal v-model="editPasswordModal" :closable='false' :mask-closable=false :width="500">
      <h3 slot="header" style="color:#2D8CF0">修改密码</h3>
      <Form ref="editPasswordForm" :model="editPasswordForm" :label-width="100" label-position="right"
            :rules="passwordValidate">
        <FormItem label="原密码" prop="oldPass" :error="oldPassError">
          <Input v-model="editPasswordForm.oldPass" placeholder="请输入现在使用的密码"/>
        </FormItem>
        <FormItem label="新密码" prop="newPass">
          <Input v-model="editPasswordForm.newPass" placeholder="请输入新密码，至少6位字符"/>
        </FormItem>
        <FormItem label="确认新密码" prop="rePass">
          <Input v-model="editPasswordForm.rePass" placeholder="请再次输入新密码"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelEditPass">取消</Button>
        <Button type="primary" :loading="savePassLoading" @click="saveEditPass">保存</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
  export default {
    name: 'Profile',
    data () {
      const validePhone = (rule, value, callback) => {
        var re = /^1[0-9]{10}$/;
        if (!re.test(value)) {
          callback(new Error('请输入正确格式的手机号'));
        } else {
          callback();
        }
      };
      const valideRePassword = (rule, value, callback) => {
        if (value !== this.editPasswordForm.newPass) {
          callback(new Error('两次输入密码不一致'));
        } else {
          callback();
        }
      };
      return {
        userForm: {
          username:'',
          name: '',
          cellphone: '',
          email: '',
          gender:'',
          birthday: '',
          w1:0,
          w2:0,
        },
        save_loading: false,
        editPasswordModal: false, // 修改密码模态框显示
        savePassLoading: false,
        oldPassError: '',
        checkIdentifyCodeLoading: false,
        w1:0,
        w2:0,
        inforValidate: {
          name: [
            { required: true, message: '请输入姓名', trigger: 'blur' }
          ],
          cellphone: [
            { required: true, message: '请输入手机号码' },
            { validator: validePhone }
          ],
          email: [
            { required: true, message: '常用邮箱不能为空', trigger: 'blur' },
            { type: 'email', message: '不正确的邮箱格式', trigger: 'blur' }
          ],
          gender: [
            { required: true, message: '请选择用户性别', trigger: 'change' }
          ],
          date: [
            { required: true, type: 'date', message: '请选择出生日期', trigger: 'change' }
          ],
        },
        editPasswordForm: {
          oldPass: '',
          newPass: '',
          rePass: ''
        },
        passwordValidate: {
          oldPass: [
            { required: true, message: '请输入原密码', trigger: 'blur' }
          ],
          newPass: [
            { required: true, message: '请输入新密码', trigger: 'blur' },
            { min: 6, message: '请至少输入6个字符', trigger: 'blur' },
            { max: 32, message: '最多输入32个字符', trigger: 'blur' }
          ],
          rePass: [
            { required: true, message: '请再次输入新密码', trigger: 'blur' },
            { validator: valideRePassword, trigger: 'blur' }
          ]
        },
        initPhone: '',
      };
    },
    methods: {
      showEditPassword () {
        this.editPasswordModal = true;
      },
      cancelEditUserInfor () {
        this.$router.push('/50ETF');
      },
      saveEdit () {
        this.$refs['userForm'].validate((valid) => {
          if (valid) {
            this.save_loading = true;
            //异步请求修改信息
            let tempUser=JSON.parse(JSON.stringify(this.userForm));
            delete tempUser.birthday;
            tempUser.birthday=this.userForm.birthday.getTime();
            delete tempUser.w1;
            delete tempUser.w2;
            this.axios.post("/backend/user/modifyInfo",tempUser)
              .then((res)=>{
                this.save_loading = false;
                if(res.data.code===0){
                  this.$Message.success("个人信息修改成功");
                }else{
                  this.$Message.error("出现了不可预料的错误");
                }
              });
          }
        });
      },
      cancelEditPass () {
        this.editPasswordModal = false;
      },
      saveEditPass () {
        this.$refs['editPasswordForm'].validate((valid) => {
          if (valid) {
            this.savePassLoading = true;
            // 异步请求修改密码
            this.axios.post("/backend/user/resetPassword",{
              oldPassword:this.editPasswordForm.oldPass,
              newPassword:this.editPasswordForm.newPass
            }).then((res)=>{
              if(res.data.code===0){
                this.$Message.success("密码更改成功");
              }else{
                this.$Message.error("出现了不可预料的错误");
              }
            })
          }
        });
      },
    },
    mounted () {
      this.userForm.username='叫我小强';
      this.userForm.name = '郭玉玲';
      this.userForm.cellphone = '18888888888';
      this.initPhone = '18888888888';
      this.userForm.email = 'xiaoqiang@163.com';
      this.userForm.gender = 'female';
      //初始化个人信息
      this.axios.post("/backend/user/getInfo")
        .then((res)=>{
          this.userForm=res.data;
        })
    }
  };
</script>

<style scoped>
  .own-space-btn-box {
    margin-bottom: 10px;
  }
  .own-space-btn-box button {
    padding-left: 0;
  }
  .own-space-btn-box button span {
    color: #2D8CF0;
    transition: all 0.2s;
  }
  .own-space-btn-box button span:hover {
    color: #0C25F1;
    transition: all 0.2s;
  }
  .own-space-tra {
    width: 10px;
    height: 10px;
    transform: rotate(45deg);
    position: absolute;
    top: 50%;
    margin-top: -6px;
    left: -3px;
    box-shadow: 0 0 2px 3px rgba(0, 0, 0, 0.1);
    background-color: white;
    z-index: 100;
  }
  .own-space-input-identifycode-con {
    position: absolute;
    width: 200px;
    height: 100px;
    right: -220px;
    top: 50%;
    margin-top: -50px;
    border-radius: 4px;
    box-shadow: 0 0 2px 3px rgba(0, 0, 0, 0.1);
  }

</style>

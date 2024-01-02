<template>
	<view main>
		<view class="bc">
			<view class="logo">
				<image src="../../static/home/logo.png" mode="" class="img"></image>
			</view>
		</view>
		<!-- 登录表单 -->
		<view class="form">
			<u--form labelPosition="left" :model="form" ref="r_form" prop="r_form">
				<u-form-item label="手机号" prop="phone" borderBottom ref="item1">
					<u-input v-model="form.phone" border="none" placeholder="请输入手机号" />
				</u-form-item>
				<view class="item">
					<u-form-item label="密码" prop="password" borderBottom ref="item1">
						<u-input v-model="form.password" border="none" type='password' placeholder="密码长度在6-15位" />
					</u-form-item>
				</view>

			</u--form>
			<!-- 按钮 -->
			<button type="primary" class="btn-one" @click="submit">登录/注册</button>
			<button type="primary" class="btn-one" style="background-color: #18BC37; margin-top: 30rpx;"
				@click="wechatLogin">微信一键登录</button>
		</view>
	</view>
</template>

<script>
	import Vue from 'vue'
	export default {
		// onShow() {
		// // #ifdef MP-WEIXIN  
		// if(wx.hideHomeButton){  
		//     wx.hideHomeButton();  
		// }  
		// // #endif
		// },
		onLoad() {
			console.log(uni.getStorageSync("vector"))
		},
		data() {
			return {
				form: {
					phone: "",
					password: ""
				},
				rules: {
					phone: [
						//对 phone 进行长度验证
						{
							required: true,
							message: '请输入手机号',
							trigger: 'change'
						},
						{
							// 自定义验证函数
							validator: (rule, value, callback) => {
								// 返回true表示校验通过，返回false表示不通过
								return this.$u.test.mobile(value);
							},
							message: '手机号码不正确',
							// 触发器可以同时用blur和change
							trigger: ['change', 'blur']
						}
					],
					password: [{
						required: true,
						min: 6,
						max: 15,
						message: '密码长度在 6 ~ 15位之间',
						trigger: 'change'
					}]

				}
			}
		},
		onReady() {
			this.$refs.r_form.setRules(this.rules)
		},
		methods: {
			//表单校验
			checkForm() {
				var flag1 = this.$u.test.mobile(this.form.phone);
				var flag2 = (this.form.password.length >= 6) && (this.form.password.length < 13);
				if (flag1 && flag2) {
					return true;
				} else {
					return false;
				}
			},
			submit() {
				var valid = this.checkForm(this.form.phone, this.form.password);
				if (valid) {
					this.login();
				} else {
					this.$refs.r_form.validate()
				}
			},
			//账号密码登录
			login() {
				uni.showLoading({
					title: "登录中..."
				})
				uni.request({
					url: Vue.prototype.urlhead + "/login/registerOrLogin",
					method: "POST",
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						phone: this.form.phone,
						password: this.form.password
					},
					success: (res) => {
						uni.hideLoading()
						if(res.data == ""){
							uni.showToast({
								title:"密码错误",
								icon:"error",
								duration: 1000
							})
						}else{
							uni.setStorageSync('userid', res.data.u_userid);
							console.log(res)
							if (res.data.u_openid == null) { //并没有绑定微信
								uni.setStorageSync("wx","-1")
								uni.reLaunch({
									url: "../../pages/home/home"
								})
							} else {
								uni.setStorageSync("wx","1")
								uni.reLaunch({
									url: "../../pages/home/home"
								})
							}
						}
					}
				})
			},
			//微信一键登录按钮
			wechatLogin(e) {
				uni.showToast({
					icon:"loading",
					duration:500
				})
				uni.getUserProfile({
					desc: "获取您的微信信息用于登录",
					success: function(resp) {
						uni.showLoading({
							title: "登录中..."
						})
						console.log(resp);
						uni.login({
							success: function(resl) {
								if (resl.code) {
									console.log(resl)
									//优化：服务器完成
									uni.request({
										url: Vue.prototype.urlhead + "/login/wechatLogin",
										method: "POST",
										header: {
											'content-type': 'application/x-www-form-urlencoded'
										},
										data: {
											code: resl.code,
											username: resp.userInfo.nickName,
											imageurl: resp.userInfo.avatarUrl
										},
										success: function(resr) {
											uni.hideLoading()
											console.log(resr);
											uni.setStorageSync('userid', resr.data
												.u_userid);
											uni.setStorageSync('wx_img', resp.userInfo
												.avatarUrl);
											uni.setStorageSync("wx","1")
											uni.switchTab({
												url: "../../pages/home/home"
											})
										},
										fail: function(err) {
											uni.hideLoading()
											console.log(err)
											uni.showModal({
												title: "错误",
												content: "服务器连接失败"
											})
										}
									})
								}
							},
							fail: function(errl) {
								uni.hideLoading()
								console.log(errl)
								uni.showModal({
									title: "提示",
									content: "登录失败"
								})
							}
						});
					},
					fail: function(errp) {
						uni.hideLoading()
						uni.showModal({
							title: "提示",
							content: "未得到授权"
						})
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.bc {
		position: relative;
		width: 750rpx;
		height: 366rpx;
		line-height: 40rpx;
		border-radius: 0rpx 0rpx 290rpx 290rpx;
		background-color: rgba(51, 136, 236, 100);

		.logo {
			position: absolute;
			top: 80rpx;
			left: 50%;
			transform: translateX(-50%);
			width: 200rpx;
			height: 200rpx;
			line-height: 40rpx;
			color: rgba(16, 16, 16, 100);
			background-color: #FFFFFF;
			border-radius: 50%;
			.img {
				width: 100%;
				height: 100%;
				border-radius: 50%;
				margin-left: 50%;
				transform: translateX(-50%);
			}
		}
	}

	.form {
		width: 70%;
		padding-left: 100rpx;
		padding-right: 100rpx;
		margin-top: 100rpx;

		/deep/.u-form-item__body__left__content__label {
			font-size: 26rpx;
			font-weight: bold;
			white-space: nowrap;
			margin-right: 40rpx;
		}

		/deep/.u-input {
			margin-left: 100rpx;
			font-size: 20rpx !important
		}
	}

	.btn-one {
		background-color: rgba(59, 157, 236, 100);
		color: rgba(255, 255, 255, 100);
		font-size: 28rpx;
		text-align: center;
		width: 440rpx;
		height: 74rpx;
		line-height: 74rpx;
		margin-top: 60rpx;
		margin-left: 50%;
		transform: translateX(-50%);
		border-radius: 64rpx;
	}
</style>

<template>
	<view>
		<view class="forms">
			<u--form labelPosition="left" :model="model1" ref="form1">
				<u-form-item label="昵称:" prop="userInfo.name" borderBottom ref="item1">
					<view style="position: relative;">
						<text class="content">{{username}}</text>
						<text class="btn" @click="open">修改</text>
					</view>
				</u-form-item>
				<u-form-item label="手机号:" prop="userInfo.name" borderBottom ref="item1">
					<view style="position: relative;">
						<text class="content">{{phone}}</text>
						<text class="btn" @click="gotochanggephone" v-if="phone==''">绑定手机</text>
						<text class="btn" @click="wxbind" v-if="wx=='-1'"></text>
					</view>
				</u-form-item>
				<u-form-item label="密码:" prop="userInfo.name" borderBottom ref="item1" v-if="password!=''">
					<view style="position: relative;">
						<text class="content">{{password_show}}</text>
						<text class="btn" @click="gotochanggepass">修改</text>
					</view>
				</u-form-item>

			</u--form>
			<uni-popup ref="popup" type="dialog">
				<uni-popup-dialog mode="input" title="修改昵称" placeholder="请输入内容" :value="value" :before-close="true"
					@close="close" @confirm="changeName">
				</uni-popup-dialog>
			</uni-popup>
		</view>
	</view>
</template>

<script>
	import Vue from 'vue'
	export default {
		data() {
			return {
				username: "一万亿颗星",
				phone: "",
				password: "",
				password_show:"",
				wx:""
			};
		},
		onLoad(params) {
			console.log(params)
			this.username = params.username;
			if(params.phone!="null"){
				this.phone = params.phone
			}
			if(params.password!="null"){
				this.password_show = "***********"
				this.password = params.password
			}
			this.wx = uni.getStorageSync("wx")
		},
		methods: {
			// 去修改密码界面
			gotochanggepass() {
				uni.navigateTo({
					url: '../changgepassword/changgepassword?password=' + this.password
				})
			},
			gotochanggephone() {
				uni.navigateTo({
					url: '../changgepnone/changgepnone'
				})
			},
			open(val) {
				var that = this;
				that.value = val;
				that.$refs.popup.open()
			},
			close(e) {
				this.$refs.popup.close()
			},
			changeName(e) {
				var that = this;
				setTimeout(function() {
					that.$refs.popup.close()
				}, 1000)
				uni.request({
					url: Vue.prototype.urlhead+"/mine/changeMes",
					method: "POST",
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						userid: uni.getStorageSync("userid"),
						key: "u_username",
						value: e
					},
					success: (res) => {
						uni.showToast({
							title: "修改成功",
							icon: "success",
							duration: 1500
						})
						this.username = e
					}
				})
			},
			wxbind(){
				uni.getUserProfile({
					desc: "获取您的微信信息",
					success: function(resp) {
						uni.login({
							success: function(resl) {
								if (resl.code) {
									console.log(resl)
									//优化：服务器完成
									uni.request({
										url: Vue.prototype.urlhead + "/mine/wxBind",
										method: "POST",
										header: {
											'content-type': 'application/x-www-form-urlencoded'
										},
										data: {
											code: resl.code,
											userid:uni.getStorageSync("userid"),
											username: resp.userInfo.nickName,
											imageurl: resp.userInfo.avatarUrl
										},
										success: function(resr) {
											console.log(resr);
											if(resr.data == "102"){
												uni.showModal({
													title:"提示",
													content:"您的微信已被用于登录，请直接使用微信登录",
													confirmText:"重新登录",
													cancelText:"取消",
													success: (sm) => {
														if(sm.confirm){
															uni.clearStorageSync();
															uni.reLaunch({
																url:"../login/login"
															})
														}
													}
												})
											}else{
												uni.setStorageSync('wx_img', resp.userInfo.avatarUrl);
												uni.setStorageSync("wx","1")
											}
										}
									})
								}
							}
						});
					}
				})
			}
		},
	}
</script>

<style lang="scss" scoped>
	.forms {
		padding-left: 30rpx;
		padding-right: 30rpx;
		margin-top: 20rpx;
		width: 85%;
		height: 200rpx;
		position: relative;

		margin-left: 20rpx;

		.content {
			position: absolute;
			top: -20rpx;
			left: 230rpx;
			transform: translateX(-50%);
			font-size: 24rpx;
		}

		.btn {
			position: absolute;
			top: -20rpx;
			right: 20rpx;
			font-size: 26rpx;
			color: #3388EC
		}

		/deep/.u-form-item__body__left__content__label {
			font-size: 28rpx;
			font-weight: bold;
			white-space: nowrap;
			margin-right: 40rpx;
		}

		/deep/.u-input__content__field-wrapper__field {
			font-size: 24rpx !important
		}

		/deep/.u-form-item__body {
			padding: 26rpx 0;
		}

		/deep/.u-input {
			margin-left: 100rpx;
			font-size: 24rpx;
		}
	}
</style>

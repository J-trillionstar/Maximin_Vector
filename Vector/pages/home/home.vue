<template>
	<view class="content">
		<!-- 头部 -->
		<view class="header">
			<image src="../../static/home/index_1.svg" mode="" class="img"></image>
			<view class="header-text">
				<text style="font-size: 32rpx;">决策——三支决策</text>
				<text style="font-size: 28rpx;margin-top: 60rpx;">小程序决策，快速便捷。</text>
			</view>
		</view>
		<view class="scan">
			<button type="default" size="mini" plain class="btn" @click="scancode">扫码参与</button>
		</view>
		<!-- 发起投票 -->
		<view class="center" @click="addvote">
			<text class="text">发起决策</text>
			<image src="../../static/home/index_2.svg" mode="" class="img"></image>
		</view>
		<!-- 待我处理 -->
		<view class="center-two" @click="gotopeading">
			<text class="text">待我处理</text>
			<image src="../../static/home/index_3.svg" mode="" class="img"></image>
		</view>
	</view>
</template>

<script>
	import Vue from 'vue'
	export default {
		data() {
			return {
				title: 'Hello'
			}
		},
		onLoad() {
			const userid = uni.getStorageSync('userid')
			if (!userid) {
				uni.redirectTo({
					url: '../../page-list/login/login'
				})
				uni.hideHomeButton()
			} else {
				let vectorid = uni.getStorageSync("vectorid")
				console.log(vectorid)
				if (vectorid) {
					uni.request({
						url: Vue.prototype.urlhead + "/vector/insertJoin",
						method: "POST",
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						data: {
							userid: userid,
							vectorid: vectorid
						},
						success: (res) => {
							uni.removeStorageSync("vectorid")
							if (res.statusCode == 200) {
								uni.navigateTo({
									url: "../../page-list/peading"
								})
							}
							console.log(res.data)
						}
					})
				}
			}
			if (uni.getStorageSync("wx") == "-1") { //账号密码登录
				uni.showModal({
					content:"建议您绑定微信",
					confirmText:"去绑定",
					success: (res) => {
						if(res.confirm){
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
						})}
					}
				})
			}
		},
		onShow() {
			uni.removeStorageSync("vector")
		},
		methods: {
			// 去往新增页面
			addvote() {
				uni.navigateTo({
					url: '../../page-list/addvote'
				})
			},
			// 去往待处理界面
			gotopeading() {
				uni.navigateTo({
					url: '../../page-list/peading'
				})
			},
			scancode(){
				uni.scanCode({
					scanType:"qrCode",
					success: (res) => {
						console.log(res)
						uni.request({
							url: Vue.prototype.urlhead + "/vector/insertJoin",
							method: "POST",
							header: {
								'content-type': 'application/x-www-form-urlencoded'
							},
							data: {
								userid: uni.getStorageSync("userid"),
								vectorid: res.result
							},
							success: (res) => {
								if (res.statusCode == 200) {
									uni.showToast({
										title:"参与成功"
									})
									uni.navigateTo({
										url: "../../page-list/peading"
									})
								}
								console.log(res.data)
							}
						})
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.content {
		width: 100%;
		height: 100%;
		background-color: #F6F6F6;
	}

	.header {
		width: 100%;
		height: 30%;
		background-color: #3388EC;

		.img {
			margin-left: 40rpx;
			margin-top: 40rpx;
			width: 228rpx;
			height: 228rpx;
		}

		.header-text {
			color: #FFFFFF;
			font-weight: bold;
			height: 120rpx;
			position: absolute;
			top: 80rpx;
			right: 100rpx;
			display: flex;
			flex-direction: column;
			justify-content: space-between
		}
	}
	
	.scan{
		.btn{
			background-color: #FFFFFF;
			box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
			border: 0rpx;
			margin-top: 50rpx;
			margin-left: 60%;
		}
	}
	
	.center {
		background-color: #FFFFFF;
		width: 666rpx;
		height: 180rpx;
		margin-top: 50rpx;
		margin-left: 42rpx;
		position: relative;
		line-height: 180rpx;
		border-radius: 10rpx;
		box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);

		.text {
			text-shadow: 0rpx 0rpx 5rpx grey;
			margin-left: 104rpx;
			font-size: 28rpx;
			color: #101010;
		}

		.img {
			width: 300rpx;
			height: 180rpx;
			position: absolute;
			top: 0;
			right: 0;
		}
	}

	.center-two {
		background-color: #FFFFFF;
		width: 666rpx;
		height: 180rpx;
		margin-top: 110rpx;
		margin-left: 42rpx;
		position: relative;
		line-height: 180rpx;
		border-radius: 10rpx;
		box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
		overflow: hidden;

		.text {
			text-shadow: 0rpx 0rpx 5rpx grey;
			margin-left: 104rpx;
			font-size: 28rpx;
			color: #101010;
		}

		.img {
			width: 260rpx;
			height: 200rpx;
			position: absolute;
			top: 0;
			right: 10rpx;
		}
	}
</style>

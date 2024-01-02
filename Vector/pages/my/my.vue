<template>
	<view class="main">
		<view class="header">
			<image v-show="loaded" @load="loaded = true" :src="user.u_imageurl" mode="" class="img"></image>
			<image src="../../static/other/loading.png" v-if="!loaded" class="img"></image>
			<view style="font-size: 28rpx;text-align: center;">
				<text>{{user.u_username}}</text>
			</view>
			<view class="choose-img">
				<text @click="uploadimage">更换头像</text>
			</view>
		</view>
		<!-- 设置 -->
		<view class="shezhi">
			<view class="shezhi-one" @click="gotomanage">
				<view class="bc" style="background-color: #FB8966;">
					<image src="../../static/my/shezhi.png" mode="" class="img"></image>
				</view>
				<text>决策管理</text>
			</view>
			<view class="shezhi-one" @click="gotojoin">
				<view class="bc" style="background-color: #76A1FF;">
					<image src="../../static/my/touxiang.png" mode="" class="img"></image>
				</view>
				<text>我参与的</text>
			</view>
			<view class="shezhi-one" @click="gotodraft">
				<view class="bc" style="background-color: #F07B7B;">
					<image src="../../static/my/draft_box.png" mode="" class="img"></image>
				</view>
				<text>我的草稿</text>
			</view>
			<view class="shezhi-one" @click="gotobin">
				<view class="bc" style="background-color: #4BD5B1;">
					<image src="../../static/my/huishouzha.png" mode="" class="img"></image>
				</view>
				<text>回收站</text>
			</view>
		</view>
		<!-- 底部设置区域 -->
		
		<button plain open-type="contact" style="position: fixed;margin-top:120rpx; z-index:99;height: 88rpx;width:100%; border-color: #FFFFFF;" size="default"></button>
		<view class="footer">
			<view class="footer-one" @click="gotoshezhi">
				<image src="../../static/my/one.png" mode="" class="img"></image>
				<text>设置</text>
			</view>
			<view class="footer-one" @click="service">
				<image src="../../static/my/two.png" mode="" class="img"></image>
				<text>联系客服</text>
			</view>
			<view class="footer-one" @click="suggestion">
				<image src="../../static/my/three.png" mode="" class="img"></image>
				<text>意见反馈</text>
			</view>
		</view>
		<uni-popup ref="popup" type="center">
			<view class="suggestion">
				<u--textarea v-model="vector.v_description" height="150" placeholder="请填写意见"></u--textarea>
				<button type="primary" style="background-color:#3388EC;" @click="submit">提交</button>
			</view>
		</uni-popup>
	</view>
</template>

<script>
	import Vue from 'vue'
	export default {
		data() {
			return {
				user: {
					u_username: "",
					u_imageurl: "",
					u_password: "",
					u_phone: ""
				},
				loaded:false
			};
		},
		onLoad() {
			// uni.showLoading({
			// 	title:"加载中..."
			// })
			uni.request({
				url: Vue.prototype.urlhead+"/mine/getUserInfo",
				method: "POST",
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				data: {
					userid: uni.getStorageSync("userid")
				},
				success: (res) => {
					console.log(res)
					if(res.data.u_imageurl==null){//没有图片
						res.data.u_imageurl = "../../static/initImage.png"
					}else{
						if (res.data.u_imageurl.indexOf("https") == 0){
							res.data.u_imageurl = uni.getStorageSync("wx_img");
						}else{
							res.data.u_imageurl = Vue.prototype.urlhead+"/" + res.data.u_imageurl
						}
					}
					this.user = res.data
					uni.setStorageSync("username",this.user.u_username);
					uni.hideLoading()
				}
			})
		},
		onShow(){
			if(uni.getStorageSync("username")){
				this.user.u_username = uni.getStorageSync("username")
			}
		},
		methods: {
			service(){
				
			},
			suggestion(){
				this.$refs.popup.open()
			},
			submit(){
				uni.showToast({
					title:"提交成功"
				})
			},
			gotoshezhi() {
				uni.navigateTo({
					url: '../../page-list/shezhi/shezhi?username=' + this.user.u_username + "&phone=" + this.user
						.u_phone + "&password=" + this.user.u_password
				})
			},
			gotomanage() {
				uni.navigateTo({
					url: "../../page-list/manage/manage"
				})
			},
			gotojoin() {
				uni.navigateTo({
					url: "../../page-list/join/join"
				})
			},
			gotodraft() {
				uni.navigateTo({
					url: `../../page-list/draft/draft`
				})
			},
			gotobin() {
				uni.navigateTo({
					url: '../../page-list/bin/bin'
				})
			},
			uploadimage(e) {
				uni.chooseImage({
					count:1,
					success: (chooseImagePath) => {
						uni.showLoading({
							title:"上传中..."
						})
						const path = chooseImagePath.tempFilePaths;
						console.log(path);
						uni.uploadFile({
							url: Vue.prototype.urlhead+'/mine/uploadImage',
							filePath: path[0],
							name: 'file',
							formData: {
								userid: uni.getStorageSync("userid")
							},
							timeout:30000,
							success: (res2) => {
								console.log(res2)
								uni.hideLoading()
								if (res2.statusCode == 200) {
									this.loaded = false
									uni.showToast({
										title: '修改成功',
										duration: 1000
									})
									console.log("修改成功")
									this.user.u_imageurl = Vue.prototype.urlhead+"/" + res2.data
									uni.setStorageSync("wx_img",res2.data)
									this.$forceUpdate()
								}
							},
							fail:()=>{
								uni.hideLoading()
								uni.showToast({
									icon:"error",
									title:"错误"
								})
							}
						})
					}
				})
			},
		},

	}
</script>

<style lang="scss" scoped>
	.main {
		width: 100%;
		height: 100%;
		background-color: #F6F6F6;
	}
	.suggestion{
		width: 600rpx;
		
	}
	.header {
		background-color: #FFFFFF;
		width: 100%;
		height: 30%;

		.img {
			width: 198rpx;
			height: 198rpx;
			border-radius: 50%;
			margin-left: 50%;
			transform: translateX(-50%);
			margin-top: 20rpx;
		}

		.choose-img {
			text-decoration: underline;
			font-size: 28rpx;
			color: #3388EC;
			margin-top: 10rpx;
			width: 100%;
			text-align: center;
		}
	}

	.shezhi {
		margin-top: 20rpx;
		padding-top: 15rpx;
		padding-bottom: 15rpx;
		background-color: #FFFFFF;
		display: flex;
		justify-content: space-around;

		.shezhi-one {
			flex: 1;
			text-align: center;
			font-size: 24rpx;

			.bc {
				margin-left: 58rpx;
				margin-bottom: 10rpx;
				display: flex;
				justify-content: center;
				align-items: center;
				height: 70rpx;
				width: 70rpx;
				background-color: #FB8966;
				border-radius: 16rpx;
			}

			.img {
				width: 40rpx;
				height: 40rpx;
			}
		}
	}

	.footer {
		margin-top: 20rpx;
		background-color: #FFFFFF;
		display: flex;
		flex-direction: column;

		.footer-one {
			position: relative;
			font-size: 30rpx;
			width: 100%;
			height: 96rpx;
			line-height: 96rpx;
			border-bottom: 2rpx solid #e1e1e1;

			.img {
				position: relative;
				top: 10rpx;
				left: 30rpx;
				width: 44rpx;
				height: 44rpx;
				margin-right: 50rpx;
			}
		}
	}
</style>

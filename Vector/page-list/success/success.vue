<template>
	<view class="main">
		<view class="img">
			<image src="../../static/other/success.png" mode="aspectFit"></image>
		</view>
		<view class="txt" style="margin-top: -70rpx;">
			<text>发布成功</text>
		</view>
		<view class="codeImg">
			<image src="../../static/other/loading.png" mode="aspectFit" style="width: 150rpx;" v-if="!loaded"></image>
			<image :src="codeImg" mode="aspectFit" @load="loaded = true" v-show="loaded"></image>
		</view>
		<view class="save" style="display: flex;justify-content: center;">
			<button type="default" size="mini" @click="download">保存二维码</button>
		</view>
		<view class="btn">
			<button type="primary" plain="true" size="mini" class="btn-one" @click="gotohome">返回首页</button>
			<button style="color: #3C9CFF;border-color: #3C9CFF;" plain="true" size="mini" class="btn-one"
				open-type="share">分享决策</button>
		</view>
	</view>
</template>

<script>
	import Vue from "vue"
	export default {
		data(){
			return{
				v_vectorid:"",
				codeImg:"",
				covImg:"",
				loaded:false
			}
		},
		onShareAppMessage(res) {
			console.log(res)
			if (res.from == 'button') {
				console.log(res)
				var vector = JSON.parse(uni.getStorageSync("vector").split(","))
				this.v_vectorid = vector.v_vectorid
				return {
					title: vector.v_name,
					imageUrl: this.covImg||this.codeImg,
					path: '/pages/home/home?vectorid='+this.v_vectorid
				}
			} else {
				return {
					title: '投票小程序',
					path: '/pages/home/home'
				}
			}
		},
		onPullDownRefresh() {
			this.getCode(this.v_vectorid)
		},
		onLoad(params) {
			this.v_vectorid = params.vectorid
			this.getCode(this.v_vectorid)
			this.covImg = params.covImg
			// uni.request({
			// 	url:Vue.prototype.urlhead+"/util/getImgCode?vectorid="+params.vectorid,
			// 	success: (res) => {
			// 		console.log(res)
			// 		this.codeImg = res.data.data.qrCodeUrl
			// 	}
			// })
		},
		methods: {
			getCode(vid){
				uni.request({
					url:Vue.prototype.urlhead+"/util/getImgCode?vectorid="+vid,
					success: (res) => {
						console.log(res)
						this.codeImg = res.data.data.qrCodeUrl
					}
				})
			},
			gotohome() {
				uni.switchTab({
					url: "../../pages/home/home"
				})
			},
			download(){
				uni.downloadFile({
					url:this.codeImg,
					success:(res)=>{
						uni.saveImageToPhotosAlbum({
							filePath:res.tempFilePath,
							success: (suc) => {
								console.log(suc)
								uni.showToast({
									title: '保存成功'
								})
							}
						})
					}
				})
			}
		}
	}
</script>

<style>
	.img {
		display: flex;
		/*设置为flex布局*/
		justify-content: center;
		/*水平居中*/
	}
	.codeImg{
		display: flex;
		/*设置为flex布局*/
		justify-content: center;
		/*水平居中*/
	}
	
	.txt {
		text-align: center;
	}

	.btn {
		display: flex;
		justify-content: center;
		/* margin-top: 30rpx; */
	}

	.btn-one {
		margin: 30rpx;
	}
</style>

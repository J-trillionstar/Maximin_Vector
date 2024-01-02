<template>
	<view class="main">
		<!-- 上传图片 -->
		<view class="uploadfile">
			<!-- <view>图片上传</view> -->
			<view style="margin-top: 20rpx;">
				<view class="img" v-for="(url, i) in candidate.imgThuList" :key='i' @click="viewImage(i)">
					<image v-show="imgload[i]" @load="loaded(i)" style="width: 100%;height: 100%;" :src="url"></image>
					<view class="loading" v-if="!imgload[i]">
						<uni-icons type="spinner-cycle" size="30"></uni-icons>
					</view>
					<view @click.stop="delImage(i)" style="display: inline;">
						<uni-icons type="closeempty" class="close" size="20"></uni-icons>
					</view>
				</view>
				<view v-if="candidate.imgThuList.length<=6" class="camera" @tap="chooseImage">
					<uni-icons type="camera" size="40"></uni-icons>
				</view>
			</view>
		</view>
		<!-- 表单 -->
		<view class="form">
			<view class="list-one">
				<text style="font-weight: bold;">选项名称:</text>
				<input type="text" v-model="candidate.c_name" placeholder="请输入选手名称" style="margin-right: 20rpx;width: 300rpx;" />
			</view>
			<view class="list-two">
				<view class="one">
					<text style="font-weight: bold;font-size: 32rpx;">选项介绍：</text>
				</view>
				<view class="two">
					<u--textarea v-model="candidate.c_introduce" placeholder="请输入内容" height='150'></u--textarea>
				</view>
			</view>
		</view>
		<button type="primary" class="btn-one" @click="determin">确定</button>
	</view>
</template>

<script>
	import Vue from "vue"
	import Util from "../../Util.js"
	export default {
		data() {
			return {
				vector: {
					v_name: "",
					v_endtime: "",
					v_description: "",
					v_ispublic: false,
					v_islive: false,
					v_isanyo: false,
					candidates: []
				},
				candidate: {
					c_name: "",
					c_introduce: "",
					c_candidateid:"",
					imgUrlList: [],
					imgThuList: [],
					sourceImgList:[]
				},
				tempFilePath:[],
				imgload:[],
				new_tag: true,
				index: -1,
				num:1
			};
		},
		onLoad(params) {
			if (uni.getStorageInfoSync("vector")) {
				this.vector = JSON.parse(uni.getStorageSync("vector").split(","))
			}
			if (params.index) {
				this.new_tag = false
				this.index = params.index
				this.candidate = (this.vector.candidates)[params.index]
				for (let i = 0; i < this.candidate.imgThuList.length; i++) {
					this.imgload.push(false)
				}
			}
		},
		methods: {
			loaded(i){
				console.log(i)
				this.imgload[i] = true;
				this.$forceUpdate()
			},
			chooseImage() {
				uni.chooseImage({
					count: 6, //最多选择 6 张
					success: (res) => {
						// if(this.tempFilePath.length==0){
						// 	this.tempFilePath = res.tempFilePaths
						// }else{
						// 	this.tempFilePath = this.tempFilePath.concat(res.tempFilePaths)
						// }
						this.candidate.imgUrlList = this.candidate.imgUrlList.concat(res.tempFilePaths)
						this.candidate.imgThuList = this.candidate.imgThuList.concat(res.tempFilePaths)
					}
				})
			},
			uploadImage(imgurl,size){
				uni.uploadFile({
					url: Vue.prototype.urlhead+"/vector/uploadCanImg",
					filePath: imgurl,
					name: 'file',
					formData: {
						vectorid: this.vector.v_vectorid,
						candidateid: this.candidate.c_candidateid
					},
					timeout:30000,
					success: (res) => {
						console.log(res)
						if(this.num == size){
							uni.hideLoading()
							uni.navigateBack({
								url: "../addvote"
							})
						}
						this.num++
					},
					fail:()=>{
						uni.hideLoading()
						uni.showToast({
							icon:"error",
							title:"上传出错"
						})
					}
				})
			},
			viewImage(i) {
				uni.previewImage({
					current: i,
					urls: this.candidate.imgUrlList
				})
			},
			delImage(i) {
				this.candidate.imgUrlList.splice(i, 1);
				this.candidate.imgThuList.splice(i, 1);
			},
			determin() {
				if(this.candidate.c_name ==""){
					uni.showToast({
						title:"选项名不能为空",
						icon:"error"
					})
				}else{
					//判断候选人是否重名
					var same = false;
					for (let i in this.vector.candidates) {
						if(i!=this.index && this.vector.candidates[i].c_name == this.candidate.c_name){
							same = true;
							break;
						}
					}
					if(same){
						uni.showModal({
							title:"提示",
							content:"该候选人已经存在，是否继续",
							success: (res) => {
								this.add()
							}
						})
					}else{
						this.add()
					}
				}
			},
			clearImg(canid){
				for(let k =0;k<this.candidate.imgUrlList.length;k++){
					var imgurl = this.candidate.imgUrlList[k]
					if (imgurl.indexOf(Vue.prototype.urlhead) === 0){
						console.log(imgurl)
						console.log(imgurl.slice((Vue.prototype.urlhead).length+1))
						this.candidate.sourceImgList.push(imgurl.slice((Vue.prototype.urlhead).length+1))
					}else{
						this.tempFilePath.push(imgurl);
					}
				}
				console.log(this.candidate.sourceImgList)
				uni.request({
					url: Vue.prototype.urlhead+"/vector/CanImgClear",
					method:"POST",
					data: {
						imgUrlList:this.candidate.sourceImgList,
						imgurl:"Vector/"+this.vector.v_vectorid+"/"+canid
					},
					success: (res) => {
						console.log(res)
						if(this.tempFilePath.length==0){
							uni.hideLoading()
							uni.navigateBack({
								url: "../addvote"
							})
						}else{
							for(var i = 0;i<this.tempFilePath.length;i++){
								this.uploadImage(this.tempFilePath[i],this.tempFilePath.length)
							}
						}
					}
				})
			},
			add(){
				if (this.new_tag) {
					let uuid = Util.getuuid();
					uuid.then((res)=>{
						this.candidate.c_candidateid = res;
						this.vector.candidates.push(this.candidate)
						uni.setStorageSync("vector",JSON.stringify(this.vector))
						uni.showLoading({
							title: "上传中"
						})
						this.clearImg(this.candidate.c_candidateid)
					})
				} else {
					this.vector.candidates[this.index] = this.candidate
					uni.setStorageSync("vector",JSON.stringify(this.vector))
					uni.showLoading({
						title: "上传中"
					})
					this.clearImg(this.candidate.c_candidateid)
				}

			}
		}

	}
</script>

<style lang="scss" scoped>
	.main {
		// background-color: #F6F6F6;
		width: 100%;
		height: 100%;
		overflow-y: scroll;
		padding-left: 30rpx;
		padding-right: 30rpx;
	}

	.uploadfile {
		margin-top: 40rpx;

		width: 100%;
		margin-bottom: 40rpx;
	}

	.img {
		position: relative;
		display: inline-block;
		width: 166rpx;
		height: 166rpx;
		vertical-align: top;
		margin-right: 10rpx;
		margin-bottom: 10rpx;
	}

	.close {
		position: absolute;
		right: 0;
		background-color: rgba(0, 0, 0, .4);
		color: #fff;
	}

	.loading{
		display: inline-block;
		width: 166rpx;
		height: 166rpx;
		text-align: center;
		line-height: 166rpx;
	}
	
	.camera {
		display: inline-block;
		width: 166rpx;
		height: 166rpx;
		text-align: center;
		line-height: 166rpx;
		border: 1rpx dotted #8C9697;
		color: #8C9697;
	}

	.form {
		padding-left: 10rpx;
		padding-right: 10rpx;

		.list-one {
			display: flex;
			justify-content: space-between;
			align-items: center;
			font-size:32rpx;
			height: 60rpx;
			line-height: 60rpx;
			width: 90%;
			border-bottom: 2rpx solid rgba(236, 236, 236, 100)
		}

		.list-two {
			width: 90%;
			margin-top: 60rpx;

			.two {
				margin-top: 20rpx;
				width: 100%;

			}
		}
	}

	.btn-one {
		border-radius: 8rpx;
		background-color: rgba(59, 157, 236, 100);
		color: rgba(255, 255, 255, 100);
		font-size: 28rpx;
		text-align: center;
		width: 540rpx;
		height: 60rpx;
		line-height: 60rpx;
		margin-top: 80rpx;
		margin-left: 80rpx;
	}
</style>

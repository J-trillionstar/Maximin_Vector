<template>
	<view class="main">
		<view class="bc">
		</view>
		<view class="form">
			<u--form labelPosition="left" :model="vector" ref="form1">
				<view class="title">
					<u-form-item label="标题:" prop="vector.v_name" borderBottom ref="item1">
						<u--input v-model="vector.v_name" border="none" placeholder="请输入标题"></u--input>
					</u-form-item>
				</view>
				<u-form-item label="结束时间:" prop="vector.v_endtime" borderBottom @click="showdate = true" ref="item1">
					<u--input disabled disabledColor="#ffffff" placeholder="请选择日期" border="none" v-model="date" />
					<u-icon slot="right" name="calendar" />
				</u-form-item>
				<view class="textarea">
					<u-form-item label=" " labelWidth="0" prop="vector.v_description" borderBottom ref="item1">
						<text style="font-size: 30rpx;font-weight: bold;margin-bottom: 20rpx;margin-left: 20rpx;">补充描述:</text>
						<u--textarea v-model="vector.v_description" placeholder="请输入内容"></u--textarea>
					</u-form-item>
				</view>
				<u-form-item label="选项:" borderBottom ref="item1">
					<view class="" style="display: flex;flex-wrap: wrap;">
						<view class="tags" v-for="(item,i) in vector.candidates" :key="i">
							<view style="margin-top: -15rpx;">
								<u-tag class="tag" :text="item.c_name" size="mini" closable @close="close = delTag(i)"
									@click="detail(i)" />
							</view>
						</view>
						<view class="tag" @click="addoption">
							+ 标签
						</view>
					</view>
				</u-form-item>
			</u--form>
			<u-datetime-picker :show="showdate" v-model="time" mode="date" @confirm="choosedate" @cancel="closedate">
			</u-datetime-picker>
		</view>
		<view class="forms">
			<u--form labelPosition="left" :model="vector" ref="form1">
				<u-form-item label="公开:" prop="vector.v_ispublic" borderBottom ref="item1">
					<view class="" style="width: 100%;position: relative;height: 30rpx;">
						<view class="" style="position: absolute; top: -10rpx; right: 20rpx;">
							<u-switch v-model="vector.v_ispublic" @change="public" activeColor=" #4cd964" size="20" />
						</view>
					</view>
				</u-form-item>
				<u-form-item label="现场决策:" prop="vector.v_islive" borderBottom ref="item1">
					<view class="" style="width: 100%;position: relative;height: 30rpx;">
						<view class="" style="position: absolute; top: -10rpx; right: 20rpx;">
							<u-switch v-model="vector.v_islive" @change="live" activeColor=" #4cd964" size="20" />
						</view>
					</view>
				</u-form-item>
				<u-form-item label="匿名决策:" prop="vector.v_isanyo" borderBottom ref="item1">
					<view class="" style="width: 100%;position: relative;height: 30rpx;">
						<view class="" style="position: absolute; top: -10rpx; right: 20rpx;">
							<u-switch v-model="vector.v_isanyo" @change="anyo" activeColor=" #4cd964" size="20" />
						</view>
					</view>
				</u-form-item>

			</u--form>
		</view>
		<view class="btns">
			<button type="primary" class="btn" @click="changeshow">发布</button>
			<button type="default" class="btn" @click="draftOrPublish(1,false)"
				style="background-color: rgb(0, 178, 106);">保存草稿</button>
		</view>

		<view>
			<u-popup :show="show" mode="center" closeOnClickOverlay @close="close" round="10">
				<view class="pop">
					<view class="p_title">
						<text>是否要为决策添加封面？</text>
					</view>
					<view>
						<view v-if="flag" class="p_img" @click="chooseCover">
							<image style="width: 100%;height: 100%;" :src="covImg"></image>
						</view>
						<view v-if="!flag" class="p_camera" @click="chooseCover">
							<uni-icons type="plusempty" size="30"></uni-icons>
						</view>
					</view>
					<view class="p_btn">
						<button style="background-color: #7e7e7e;color: #FFFFFF;" size="mini" class="btn-one"
							@click="draftOrPublish(2,false)">不需要</button>
						<button type="primary" size="mini" class="btn-one" :disabled="!flag"
							@click="draftOrPublish(2,true)">确定</button>
					</view>
				</view>
			</u-popup>
		</view>
	</view>
</template>

<script>
	import Vue from "vue"
	import Util from "../Util.js"
	export default {
		data() {
			return {
				vector: {
					v_vectorid: "",
					v_name: "",
					v_endtime: "",
					v_description: "",
					v_ispublic: false,
					v_islive: false,
					v_isanyo: false,
					v_location:"",
					candidates: []
				},

				showdate: false,
				time: new Date((new Date()).toLocaleDateString()).getTime(), //日期
				date: "",

				flag: false,
				show: false,
				covImg: "",

				fromdraft: false //判断是否从草稿箱进来的
			};
		},
		onLoad(params) {
			console.log("onLoad")
			console.log(params)
			this.fromdraft = false
			if (uni.getStorageSync("vector")) {
				console.log("enter")
				this.vector = JSON.parse(uni.getStorageSync("vector").split(","))
				if (this.vector.v_endtime) {
					this.date = Util.dateParse(this.vector.v_endtime,false)
					this.time = (new Date(this.date)).getTime()
				}
				if (params.fromdraft) {
					uni.request({
						url: Vue.prototype.urlhead+"/vector/queryCandidates",
						method: 'POST',
						data: {
							vectorid: this.vector.v_vectorid
						},
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						success: (res) => {
							let cans = res.data
							for (let j = 0; j < cans.length; j++) {
								cans[j].imgThuList = []
								cans[j].sourceImgList = []
								for (let i = 0; i < cans[j].imgUrlList.length; i++) {
									let str = cans[j].imgUrlList[i]
									if (str.indexOf("Vector") === 0) {
										cans[j].imgUrlList[i] = Vue.prototype.urlhead+"/" + str
										let s = str.split(".")
										cans[j].imgThuList[i] = Vue.prototype.urlhead+"/" + s[0] + Util.Thu + "." + s[1] 
									}
								}
							}
							this.vector.candidates = cans;
							uni.setStorageSync("vector",this.vector)
						}
					})
					if(this.vector.v_ispublic==1){
						this.vector.v_ispublic = true;
					}
					if(this.vector.v_islive==1){
						this.vector.v_islive = true;
					}
					if(this.vector.v_isanyo==1){
						this.vector.v_isanyo = true;
					}
				}
			}else{
				uni.request({
					url: Vue.prototype.urlhead+"/vector/creatVec",
					method:"POST",
					data: {
						vector:this.vector,
						userid:uni.getStorageSync("userid"),
						status:-2
					},
					success: (res) => {
						this.vector.v_vectorid = res.data
					}
				})
			}
			if (params.fromdraft) {
				console.log("fromdraft")
				this.fromdraft = true
			}
			console.log(this.vector)
		},
		onShow() {
			console.log("onshow")
			console.log(uni.getStorageSync("vector"))
			if (uni.getStorageSync("vector")) {
				this.vector = JSON.parse(uni.getStorageSync("vector").split(","))
				console.log(this.vector)
			}
		},
		methods: {
			close(){
				this.show = false
			},
			public(e){
				this.vector.v_ispublic = e
				uni.setStorageSync("vector",this.vector)
			},
			live(e){
				this.vector.v_islive = e
				uni.setStorageSync("vector",this.vector)
			},
			anyo(e){
				this.vector.v_isanyo = e
				uni.setStorageSync("vector",this.vector)
			},
			// 选择日期
			choosedate() {
				this.showdate = false
				setTimeout(() => {
					console.log(this.time)
					console.log(Util.dateParse(this.time+24*60*60*1000-1000,true))
					this.date = Util.dateParse(this.time,false)
					this.vector.v_endtime = this.time+24*60*60*1000-1000
				}, 1000)
			},
			closedate() {
				this.showdate = false
			},

			addoption() {
				uni.setStorageSync("vector", JSON.stringify(this.vector))
				uni.navigateTo({
					url: "addoption/addoption"
				})
			},
			detail(i) {
				uni.setStorageSync("vector", JSON.stringify(this.vector))
				uni.navigateTo({
					url: "addoption/addoption?index=" + i
				})
			},
			delTag(i) {
				this.vector.candidates.splice(i, 1);
			},
			uploadInfo(status,have){
				if(this.vector.v_name==""){
					uni.showToast({
						title:"名称不能为空",
						icon:"error"
					})
				}else{
				uni.showLoading({
					title:"上传中..."
				})
				console.log(this.vector)
				uni.removeStorage({
					key: "vector"
				})
				uni.request({
					url: Vue.prototype.urlhead+"/vector/draftOrPublish",
					data: {
						vector: this.vector,
						candidates: this.vector.candidates,
						userid: uni.getStorageSync("userid"),
						status: status
					},
					method: 'POST',
					success: (res) => {
						console.log(res)
						if (res.statusCode == 200) {
							if (status == 1) { //保存草稿
								uni.showToast({
									title: '保存成功',
									duration: 1000,
									success: () => {
										uni.hideLoading()
										setTimeout(() => {
											if (this.fromdraft
											) {
												uni.navigateBack({
													url: "draft/draft"
												})
											} else {
												uni.reLaunch({url: "../pages/home/home"})
											}
										}, 500)
									}
								})
							} else { //发布
								console.log("发布")
								console.log("have")
								console.log(this.covImg)
								if (have) {
									this.uploadCovImg();
								} else {
									uni.hideLoading()
									uni.redirectTo({
										url: "success/success?vectorid=" +this.vector.v_vectorid
									})
								}
							}
						} else {
							console.log(res)
						}
					}
				});
				}
			},
			judge(){
				if(this.vector.v_name==""){
					uni.showToast({
						title:"名称不能为空",
						icon:"error"
					})
					return false;
				}else{
					if(this.vector.candidates.length<2){
						uni.showToast({
							title:"至少2个选项",
							icon:"error"
						})
						return false;
					}if(this.vector.v_endtime == ""){
						uni.showToast({
							title:"请设置截止时间",
							icon:"error"
						})
					}else{
						return true;
					}
				}
				
			},
			draftOrPublish(status, have) {
				if(status == 2 && this.vector.v_islive){//发布
					uni.getLocation({
						isHighAccuracy:true,
						success:(res)=> {
							console.log(res)
							console.log(this.vector)
							this.vector.v_location = res.latitude+","+res.longitude
							this.uploadInfo(status,have)
						}
					})
					// let loc = Util.getLocation();
					// loc.then((res)=>{
					// 	this.vector.v_location = res.lat+","+res.lng
					// 	this.uploadInfo(status,have)
					// })
				}else{
					console.log(status)
					console.log(have)
					this.uploadInfo(status,have)
				}
			},
			changeshow(){
				console.log(this.vector)
				if(this.judge()){
					this.show = true
				}else{
					this.show = false
				}
			},
			chooseCover() {
				uni.chooseImage({
					count: 1, //选择封面
					success: (res) => {
						console.log(res.tempFilePaths)
						this.covImg = res.tempFilePaths[0]
						this.flag = true
					}
				})
			},
			uploadCovImg() {
				console.log("uploadCovImg")
				console.log(this.covImg)
				uni.uploadFile({
					url: Vue.prototype.urlhead+"/vector/uploadVecImg",
					filePath: this.covImg,
					name: 'file',
					formData: {
						vectorid: this.vector.v_vectorid
					},
					success: (res) => {
						console.log(res)
						if (res.statusCode == 200) {
							uni.hideLoading()
							uni.redirectTo({
								url: "success/success?vectorid=" + this.vector
									.v_vectorid +
									"&covImg=" + this.covImg
							})
						}
					}
				})
			}
		}
	};
</script>

<style lang="scss" scoped>
	.main {
		background-color: #F6F6F6;
		width: 100%;
		height: 100%;
		overflow-y: scroll;
	}

	.bc {
		height: 368rpx;
		width: 750rpx;
		border-radius: 0rpx 0rpx 290rpx 290rpx;
		background-color: rgba(51, 136, 236, 100);
		position: absolute;
	}

	.form {
		padding-top: 20rpx;
		padding-left: 30rpx;
		padding-right: 30rpx;
		margin-top: 40rpx;
		width: 550rpx;
		height: 650rpx;
		box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
		border: 2rpx solid rgba(187, 187, 187, 100);
		background-color: #FFFFFF;
		border-radius: 10rpx;
		position: relative;
		margin-left: 68rpx;

		/deep/.u-form-item__body__left__content__label {
			font-size: 30rpx;
			font-weight: bold;
			white-space: nowrap;
			margin-right: 40rpx;
			margin-left: 20rpx;
		}

		.title {
			/deep/.u-form-item__body__left__content__label {
				font-size: 32rpx;
			}
		}

		/deep/.u-input {
			margin-left: 100rpx;
			font-size: 24rpx;
		}

		/deep/.u-form-item__body {
			padding: 10rpx 0;
		}
	}

	.forms {
		padding-left: 30rpx;
		padding-right: 30rpx;
		margin-top: 40rpx;
		width: 550rpx;
		height: 220rpx;
		box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
		border: 2rpx solid rgba(187, 187, 187, 100);
		background-color: #FFFFFF;
		border-radius: 10rpx;
		position: relative;

		margin-left: 68rpx;

		/deep/.u-form-item__body__left__content__label {
			font-size: 28rpx;
			font-weight: bold;
			white-space: nowrap;
			margin-right: 40rpx;
			margin-left: 20rpx;
		}

		/deep/.u-input__content__field-wrapper__field {
			font-size: 24rpx !important
		}

		/deep/.u-form-item__body {
			padding: 14rpx 0;
			margin-top: 10rpx;
		}

		/deep/.u-input {
			margin-left: 100rpx;
			font-size: 24rpx;
		}
	}

	.btns {
		margin-top: 40rpx;

		.btn {
			border-radius: 8rpx;
			background-color: rgba(59, 157, 236, 100);
			color: rgba(255, 255, 255, 100);
			font-size: 24rpx;
			text-align: center;
			width: 540rpx;
			height: 60rpx;
			line-height: 60rpx;
			margin-top: 15rpx;
		}
	}
	
	.tag {
		margin-top: auto;
		margin-bottom: auto;
		width: 88rpx;
		height: 44rpx;
		border-radius: 8rpx;
		background-color: rgba(255, 255, 255, 100);
		color: rgba(42, 143, 247, 100);
		font-size: 20rpx;
		line-height: 44rpx;
		text-align: center;
		border: 2rpx solid rgba(12, 177, 255, 100);
	}

	.pop {
		width: 500rpx;
		height: 400rpx;

		.p_title {
			text-align: center;
			margin: 10rpx;
			margin-bottom: 30rpx;
		}
	}

	.p_img {
		display: flex;
		width: 166rpx;
		height: 166rpx;
		border: 1rpx dotted #8C9697;
		margin: 0 auto;
		border-radius: 30rpx;
		justify-content: center;
	}

	.p_camera {
		display: flex;
		width: 166rpx;
		height: 166rpx;
		line-height: 166rpx;
		font-size: 46rpx;
		border: 1rpx dotted #8C9697;
		color: #8C9697;
		border-radius: 30rpx;
		justify-content: center;
		margin: 0 auto;
	}

	.p_btn {
		display: flex;
		justify-content: center;
		margin-top: 20rpx;
	}
</style>

<template>
	<view class="main">
		<!-- 头部 -->
		<view class="header">
			<view class="search-one">
				<uni-search-bar @input="empty" @confirm="query" placeholder="请输入内容..." cancelButton="none" focus v-model="searchValue"></uni-search-bar>
			</view>
			<view>
				<u-picker :show="show" :columns="columns" keyName="label" @confirm="confirm" @cancel="cancel"></u-picker>
				<view class="search-people" @click="start">
					<view class="uni-input">{{columns[0][index].label}}</view>
					<u-icon name="arrow-down"></u-icon>
				</view>
			</view>

		</view>
		<!-- 内容区域 -->
		<view class="centent">
			<view class="box" v-for="(vec,i) in vectorCovers" :key="i" @click="join(vec.v_vectorid)">
				<view class="boximg">
					<image :src="vec.v_imgurl" class="img" mode="aspectFit" lazy-load="true"></image>
				</view>
				<view class="down">
					<view class="line">
						<text class="title">名称：</text>
						<text class="content">{{vec.v_name}}</text>
					</view>
					<view class="line">
						<text class="title">参与人数：</text>
						<text>{{vec.join_count}}</text>
					</view>
					<view class="line">
						<text class="title">发布时间：</text>
						<text>{{vec.v_updatetime}}</text>
					</view>
					<view class="line">
						<text class="title">截止时间：</text>
						<text>{{vec.v_endtime}}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import Vue from 'vue'
	import Util from '../../Util.js'
	export default {
		data() {
			return {
				vectorCovers: [
				// 	{
				// 	v_vectorid: "",
				// 	v_name: "",
				// 	v_imgurl: "",
				// 	join_count: "",
				// 	v_endtime: "",
				// 	v_updatetime: ""
				// }
				],
				show: false,
				index: 0,
				order:"",
				searchValue:"",
				columns: [
					[{
						label: '发布时间',
						value: 'v_updatetime'
					}, {
						label: '参与人数',
						value: 'join_count'
					}, {
						label: '截止时间',
						value: 'v_endtime'
					}]
				]
			}
		},
		onShow() {
			uni.showLoading({
				title:"加载中..."
			})
			uni.removeStorageSync("people")
			uni.removeStorageSync("vector")
			this.searchValue = ""
			this.updatepage("v_updatetime")
		},
		methods: {
			query(v){
				uni.showLoading({
					title:"加载中..."
				})
				uni.request({
					url:Vue.prototype.urlhead+"/vector/getCoverLike",
					method: 'POST',
					data: {
						order:this.order,
						name: v.value
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					timeout:30000,
					success: (res) => {
						uni.hideLoading()
						this.vectorCovers = res.data
						console.log(res.data)
						for (let i in res.data) {
							this.vectorCovers[i].v_endtime = Util.dateParse((res.data)[i].v_endtime,false)
							this.vectorCovers[i].v_updatetime = Util.dateParse((res.data)[i].v_updatetime,false)
							this.vectorCovers[i].v_imgurl = Vue.prototype.urlhead+"/"+this.vectorCovers[i].v_imgurl
						}
						this.$forceUpdate()
					},
					fail:()=>{
						uni.hideLoading()
						uni.showToast({
							icon:"error",
							title:"错误"
						})
					}
				})
			},
			empty(v){
				if(v.value==""){
					uni.showLoading({
						title:"加载中..."
					})
					this.updatepage(this.order)
				}
			},
			updatepage(order) {
				uni.request({
					url: Vue.prototype.urlhead+"/vector/queryCover",
					method: 'POST',
					data: {
						order: order
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					timeout:30000,
					success: (res) => {
						uni.hideLoading()
						console.log(res)
						this.vectorCovers = res.data
						for (let i in res.data) {
							this.vectorCovers[i].v_endtime = Util.dateParse((res.data)[i].v_endtime,false)
							this.vectorCovers[i].v_updatetime = Util.dateParse((res.data)[i].v_updatetime,false)
							this.vectorCovers[i].v_imgurl = Vue.prototype.urlhead+"/"+this.vectorCovers[i].v_imgurl
						}
						this.$forceUpdate()
					},
					fail:()=>{
						uni.hideLoading()
						uni.showToast({
							icon:"error",
							title:"错误"
						})
					}
				})
			},
			join(vectorid) {
				uni.navigateTo({
					url: "../../page-list/votedetail/votedetail?vectorid=" + vectorid + "&fromsquare=1"
				})
			},
			start() {
				this.show = true
			},
			confirm(e) {
				console.log(e)
				this.show = false
				this.index = e.indexs[0]
				console.log(this.columns[0][this.index].value)
				this.order = this.columns[0][this.index].value
				this.updatepage(this.order)
			},
			cancel(){
				this.show = false
			}
		}
	}
</script>

<style lang="scss" scoped>
	.search-people {
		width: 140rpx;
		display: flex;
		background-color: #FFFFFF;
		justify-content: space-around;
		align-items: center;
		font-size: 24rpx;
		height: 64rpx;
		border-radius: 8rpx;
	}

	.main {
		background-color: #f1f1f1;
		height: 100%;
		overflow-y: scroll;
	}

	.header {
		width: 100%;
		height: 100rpx;
		line-height: 100rpx;
		background-color: #3388EC;
		display: flex;
		justify-content: space-around;
		align-items: center;
	}

	.centent {
		display: flex;
		flex-wrap: wrap;
		padding: 20rpx;
		justify-content: space-between;

		.box {
			margin-top: 20rpx;
			width: 340rpx;
			height: 440rpx;
			background-color: #FFFFFF;
			border-radius: 20rpx;
			.boximg {
				width: 300rpx;
				height: 250rpx;
				margin: 0rpx auto;

				.img {
					width: 100%;
					height: 100%;
				}
			}

			.down {
				border-top: solid 1rpx #DCDCDC;

				.line {
					margin-top: 10rpx;
					margin-left: 10rpx;
					width: 95%;
					font-size: 28rpx;
					display: block;
					white-space: nowrap;
					overflow: hidden;
					text-overflow: ellipsis;

					.title {
						width: 100%;
						color: gray;
						font-size: 28rpx;
					}
				}
			}
		}
	}
</style>

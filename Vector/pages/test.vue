<template>
	<view>
		<u-popup :show="show" closeOnClickOverlay="false" @open="open" mode="center" round="10">
			<view class="pop">
				<view class="p_title">
					<text>是否要为投票添加封面？</text>
				</view>
				<view>
					<view v-if="flag" class="p_img">
						<image style="width: 100%;height: 100%;" :src="coverurl"></image>
					</view>
					<view v-if="!flag" class="p_camera" @tap="chooseCover">
						<uni-icons type="plusempty" size="30"></uni-icons>
					</view>
				</view>
				<view class="p_btn">
					<button style="background-color: #7e7e7e;color: #FFFFFF;" size="mini" class="btn-one" @click="donot">不需要</button>
					<button type="primary" size="mini" class="btn-one" :disabled="!flag">确定</button>
				</view>
			</view>
		</u-popup>
		<u-button @click="show = true">打开</u-button>
	</view>
</template>
<script>
	export default {
		data() {
			return {
				show: false,
				coverurl: "",
				flag: false
			}
		},
		methods: {
			open() {
				// console.log('open');
			},
			close() {
				this.show = false
				// console.log('close');
			},
			chooseCover() {
				uni.chooseImage({
					count: 1,
					success: (res) => {
						console.log(res.tempFilePaths)
						this.coverurl = res.tempFilePaths[0]
						this.flag = true;
					}
				})
			},
		}
	}
</script>
<style lang="scss" scoped>
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
		margin:0 auto;
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
		margin:0 auto;
	}
	
	.p_btn {
		display: flex;
		justify-content: center;
		margin-top: 20rpx;
	}
	
	.btn-one {
		margin: 30rpx;
	}
</style>

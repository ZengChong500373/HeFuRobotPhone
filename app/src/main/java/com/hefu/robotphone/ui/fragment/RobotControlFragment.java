package com.hefu.robotphone.ui.fragment;


import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import com.hefu.robotphone.R;
import com.hefu.robotphone.RobotApplication;
import com.hefu.robotphone.databinding.FragmentRobotControlBinding;
import com.hefu.robotphone.ui.activity.MainActivity;
import com.hefu.robotphone.utils.ConectionControl;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.ErrorCode;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.bean.EZAccessToken;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;

import hefu.robotphone.sdk.bean.TokenBean;

import com.hefu.robotphone.http.Network;
import com.videogo.realplay.RealPlayStatus;
import com.videogo.util.LogUtil;
import com.videogo.util.Utils;

import hefu.robotphone.sdk.utlis.ToastUtils;
import hefu.robotphone.uilibrary.base.LazyFragment;
import hefu.robotphone.uilibrary.customview.DirectionControlView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static hefu.robotphone.sdk.utlis.Constant.YS_APP_KEY;
import static hefu.robotphone.sdk.utlis.Constant.YS_SECRET;

public class RobotControlFragment extends LazyFragment<FragmentRobotControlBinding> implements Handler.Callback, SurfaceHolder.Callback {
    private EZCameraInfo mCameraInfo = null;
    private SurfaceHolder holder;
    /**
     * 演示点预览控制对象
     */
    private EZPlayer mEZPlayer = null;
    private EZDeviceInfo mDeviceInfo = null;
    private Handler mHandler = null;
    private Boolean isTokenOk = false;

    @Override
    public int setFragmentView() {
        return R.layout.fragment_robot_control;
    }

    @Override
    public void onFirstUserVisible() {
        jyhBinding.directionContralView.setOnDirectionListener(new DirectionControlView.OnDirectionListener() {
            @Override
            public void direction(DirectionControlView.Direction direction) {
                MainActivity.socket.goWhere(ConectionControl.getDirectionString(direction));
            }
        });
        mHandler = new Handler(this);
        holder = jyhBinding.surface.getHolder();
        holder.addCallback(this);
        CheckToken();
        jyhBinding.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckToken();
            }
        });
    }

    public void CheckToken() {
        EZAccessToken token = RobotApplication.getOpenSDK().getEZAccessToken();
        if (token == null || token.getExpire() > System.currentTimeMillis()) {
            getToken();
        } else {
            isTokenOk = true;
            startRealPlay();
        }
    }

    public void getToken() {
        Network.getYSMethods().rxGetToken(YS_APP_KEY, YS_SECRET).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<TokenBean>() {
            @Override
            public void onCompleted() {
                jyhBinding.pb.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                isTokenOk = false;
                jyhBinding.pb.setVisibility(View.GONE);
            }

            @Override
            public void onNext(TokenBean tokenBean) {
                String accessToken = tokenBean.getData().getAccessToken();
                RobotApplication.getOpenSDK().setAccessToken(accessToken);
                ToastUtils.getInstance().show("获取token 成功");
                isTokenOk = true;
                startRealPlay();
            }
        });

    }

    private void startRealPlay() {
        jyhBinding.pb.setVisibility(View.VISIBLE);
        jyhBinding.imgPlay.setVisibility(View.GONE);
        // 增加手机客户端操作信息记录
        if (isTokenOk == false) {
            return;
        }
        if (mEZPlayer == null) {
            mEZPlayer = RobotApplication.getOpenSDK().createPlayer("788411760", 1);
        }

        if (mEZPlayer == null)
            return;
        mEZPlayer.setPlayVerifyCode("CCOYDD");

        mEZPlayer.setHandler(mHandler);
        mEZPlayer.setSurfaceHold(holder);
        mEZPlayer.startRealPlay();


    }

    /**
     * 停止播放
     *
     * @see
     * @since V1.0
     */
    private void stopRealPlay() {
        if (mEZPlayer != null) {
            mEZPlayer.stopRealPlay();
        }

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:
                handlePlayFail(msg.obj);
                break;
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS:
                jyhBinding.imgPlay.setVisibility(View.GONE);
                jyhBinding.pb.setVisibility(View.GONE);
                break;
        }
        return false;
    }

    /**
     * 处理播放失败的情况
     *
     * @param obj - 错误码
     * @see
     * @since V1.0
     */
    private void handlePlayFail(Object obj) {
        int errorCode = 0;
        if (obj != null) {
            ErrorInfo errorInfo = (ErrorInfo) obj;
            errorCode = errorInfo.errorCode;
        }

        jyhBinding.imgPlay.setVisibility(View.VISIBLE);
        jyhBinding.pb.setVisibility(View.GONE);
        stopRealPlay();

        updateRealPlayFailUI(errorCode);
    }

    private void updateRealPlayFailUI(int errorCode) {
        String txt = null;
        jyhBinding.imgPlay.setVisibility(View.VISIBLE);
        jyhBinding.pb.setVisibility(View.GONE);
        // 判断返回的错误码
        switch (errorCode) {
            case ErrorCode.ERROR_TRANSF_ACCESSTOKEN_ERROR:
                getToken();
                return;
            case ErrorCode.ERROR_CAS_MSG_PU_NO_RESOURCE:
                txt = "设备连接数过大，停止其他连接后再试试吧";
                break;
            case ErrorCode.ERROR_TRANSF_DEVICE_OFFLINE:
                if (mCameraInfo != null) {
                    mCameraInfo.setIsShared(0);
                }
                txt = "设备不在线";
                break;
            case ErrorCode.ERROR_INNER_STREAM_TIMEOUT:
                txt = "播放失败，连接设备异常";
                break;
            case ErrorCode.ERROR_WEB_CODE_ERROR:
                //VerifySmsCodeUtil.openSmsVerifyDialog(Constant.SMS_VERIFY_LOGIN, this, this);
                //txt = Utils.getErrorTip(this, R.string.check_feature_code_fail, errorCode);
                break;
            case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_OP_ERROR:
                //VerifySmsCodeUtil.openSmsVerifyDialog(Constant.SMS_VERIFY_HARDWARE, this, null);
//                SecureValidate.secureValidateDialog(this, this);
                //txt = Utils.getErrorTip(this, R.string.check_feature_code_fail, errorCode);
                break;
            case ErrorCode.ERROR_TRANSF_TERMINAL_BINDING:
                txt = "请在萤石客户端关闭终端绑定";
                break;
            // 收到这两个错误码，可以弹出对话框，让用户输入密码后，重新取流预览
            case ErrorCode.ERROR_INNER_VERIFYCODE_NEED:
            case ErrorCode.ERROR_INNER_VERIFYCODE_ERROR: {
//                DataManager.getInstance().setDeviceSerialVerifyCode(mCameraInfo.getDeviceSerial(), null);

            }
            break;
            case ErrorCode.ERROR_EXTRA_SQUARE_NO_SHARING:
            default:
                txt = "视频播放失败";
                break;
        }

        if (!TextUtils.isEmpty(txt)) {
            ToastUtils.getInstance().show(txt);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(holder);
        }
        this.holder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(null);
        }
        this.holder = null;
    }
}

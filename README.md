# Compressor

图片压缩方法来源于：[Luban](https://github.com/Curzibn/Luban)
---

并没有经过同意的情况下，我对`Luban`代码进行了修改，去除了RxJava的使用，依据个人理解简单优化了一下api调用方式，如Luban的作者不爽，可以联系我删除

**使用方法**

没有依赖第三方库，需要加入下面权限

    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

Java代码

    Compressor.get(this)
        .load(imgFile)
        .listener(new OnCompressListener() {
            @Override
            public void onStart() {
                progress.setVisibility(View.VISIBLE);
            }
            @Override
            public void onSuccess(File file) {
                progress.setVisibility(View.INVISIBLE);
                int[] imgSize = Compressor.get(MainActivity.this).getImageSize(file.getPath());
                tvPicSize2.setText(String.format(Locale.ENGLISH, "缩略图大小：%1$dk  尺寸：%2$d * %3$d"
                    , file.length() / 1024, imgSize[0], imgSize[1]));
                Glide.with(MainActivity.this).load(file).into(imgPic2);
            }
            @Override
            public void onError(Throwable e) {
                progress.setVisibility(View.INVISIBLE);
                tvPicSize2.setText("压缩失败了");
            }
        }).start();

方法 | 说明
-- | --
load(file) | 传入要压缩的图片文件
listener(new OnCompressListener()) | 监听压缩进程
start() | 开始压缩
getImageSize(filePath) | 根据图片路径获取尺寸
onDestory() | 销毁未执行完毕的异步线程

**最好在使用Compressor页面的onDestory方法执行之前，调用Compressor.onDestory()，原因是Compressor异步线程用的是AsyncTask**

#License


    Copyright 2016 James

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

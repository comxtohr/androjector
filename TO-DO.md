###TO-DO

1. 优化流程
2. 降低画质

####1 优化流程

现有两套方案：

1. 设备端取得root权限后完全使用PC端adb控制（screencap + pull + show）（ver 1）
2. 设备端取得root权限后使用Android Service维护图像序列，PC端仅pull + show

####2 降低画质

现有两套方案：

1. 运行screencap 获得raw数据后在设备上缩小为1/4（push到设备中后使用PC端控制，C + 管道，bmp + gzip）
2. 利用ddmlib.jar重写PC端程序获得截图（Java）

(libpng在Android平台上移植失败，放弃使用png压缩图片改用gzip)

(降低画质的方案1已完成为2.0版本，采用screencap --> scp(降低画质) --> gzip --> pull --> gzip -d --> openCV --> rm 的流程)

(2.1版本计划将gzip -d 改为gzip -c，以减少文件读写操作以及 rm 操作)

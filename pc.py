import os
import commands
import subprocess
import cv
import struct
cmd = raw_input("Connect? y | n:")
if cmd == "y":
	ip = raw_input("IP:")
	os.system("adb connect "+ ip + ":5555")

cmd = raw_input("Push? y | n :")
if cmd == "y":
	arg0 = commands.getoutput("adb shell mount | grep /system | cut -d ' ' -f 3")
	arg1 = commands.getoutput("adb shell mount | grep /system | cut -d ' ' -f 1")
	arg2 = commands.getoutput("adb shell mount | grep /system | cut -d ' ' -f 2")

	p = subprocess.Popen("adb shell",stdin = subprocess.PIPE,stdout = subprocess.PIPE,stderr = subprocess.PIPE,shell = True)
	p.stdin.write("su\n")
	p.stdin.write("mount -o rw,remount -t " + arg0 + " " + arg1 + " " + arg2 +"\n")
	p.stdin.write("chmod 777 /system\n")
	p.stdin.write("exit\n")
	p.stdin.write("exit\n")

	os.system("adb push ~/scp/obj/local/armeabi/scp /system/")
	os.system("adb shell chmod 777 /system/scp")
#cmd = raw_input("Mode? 1 | 2 :")
#if cmd == "1":
#	cv.NamedWindow("androjector",cv.CV_WINDOE_AUTOSIZE)
#	while 1 > 0:
#		os.system("adb shell /system/bin/screencap -p /sdcard/shot.png")
#		os.system("adb pull /sdcard/shot.png ~")
#		img = cv.LoadImage("/Users/carl/shot.png")
#		cv.ShowImage("androjector",img)
#		cv.WaitKey(50)
#	cv.DestoryWindow("androjector")
#elif cmd == "2":
flag = True
while 1 > 0:
	os.system('adb shell "/system/bin/screencap | /system/scp | gzip >/mnt/sdcard/shot.gz"')
	os.system("adb pull /mnt/sdcard/shot.gz ~")
	os.system("gzip -d -f  ~/shot.gz")
	f = open("/Users/carl/shot","rb")
	try:
		if flag == True:
			width,height = struct.unpack("ii",f.read(8))
			img = cv.CreateImage((width,height),cv.IPL_DEPTH_8U,3)
		else:
			f.read(8)
		for i in range(0,height):
			for j in range(0,width):
				b,g,r = struct.unpack("BBB",f.read(3))
				cv.Set2D(img,i,j,cv.Scalar(r,g,b))
		cv.ShowImage("androjector",img)
		cv.WaitKey(20)
		flag = False
	finally:
		f.close()

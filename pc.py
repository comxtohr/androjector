import os
import time
import cv

ip = raw_input("IP:")
os.system("adb connect " + ip + ":5555") 
cmd = raw_input("start?y|n:")
if cmd == "y":
	cv.NamedWindow("window", cv.CV_WINDOW_AUTOSIZE)
	while 1 > 0:
		os.system("adb shell /system/bin/screencap -p /sdcard/screenshot.png")
		os.system("adb pull /sdcard/screenshot.png ~")
		img = cv.LoadImage("/Users/carl/screenshot.png")
		cv.ShowImage("window",img)
		cv.WaitKey(50)
	cv.DestroyWindow("window")
exit()

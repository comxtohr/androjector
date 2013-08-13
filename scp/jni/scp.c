#include <stdio.h>

void process(int,int,int,int);
int main()
{
	int buf[3];		//buf[0] : width
				//buf[1] : height
				//buf[2] : format
	fread(buf,4,3,stdin);
	buf[0] /= 2;
	buf[1] /= 2;
	fwrite(buf,4,2,stdout);
	if (buf[2] == 1 || buf[2] == 2 || buf[2] == 5)
		process(buf[0] * 2,buf[1] * 2,4,buf[2]);	//32-bit
	else if (buf[2] == 3)
		process(buf[0] * 2,buf[1] * 2,3,buf[2]);	//24-bit
	else if (buf[2] == 4 || buf[2] == 6 || buf[2] == 7)
		process(buf[0] * 2,buf[1] * 2,2,buf[2]);	//16-bit
	else
		fprintf(stderr,"can't process this pixel format #%d\n",buf[2]);
	return 0;
}

void process(int width,int height,int bits,int format)
{
	unsigned char buf[4]="";
	unsigned short r=0,g=0,b=0;
	int row,col;
	for (row = 0;row < height;row++)
	{
		for (col = 0;col < width;col++)
		{
			fread(buf,bits,1,stdin);
			if (row % 2 == 0 && col % 2 == 0)
			{
				if (format == 5)
				{
					buf[3] = buf[0];
					buf[0] = buf[2];
					buf[2] = buf[3];
				}
				if (format == 4)
				{
					r = ((buf[1] << 8 + buf[0]) & 0xF800) >> 11;
					g = ((buf[1] << 8 + buf[0]) & 0x7E0) >> 5;
					b = (buf[1] << 8 + buf[0]) & 0x1F;
					buf[0] = (unsigned char)b;
					buf[1] = (unsigned char)g;
					buf[2] = (unsigned char)r;
				}
				if (format == 6)
				{
					r = ((buf[1] << 8 + buf[0]) & 0x7C00 ) >> 10;
					g = ((buf[1] << 8 + buf[0]) & 0x3E0) >> 5;
					b = (buf[1] << 8 + buf[0]) & 0x1F;
					buf[0] = (unsigned char)b;
					buf[1] = (unsigned char)g;
					buf[2] = (unsigned char)r;
				}
				if (format == 7)
				{
					r = ((buf[1] << 8 + buf[0]) & 0xF00) >> 8;
					g = ((buf[1] << 8 + buf[0]) & 0xF0) >> 4;
					b = (buf[1] << 8 + buf[0]) & 0xF;
					buf[0] = (unsigned char)b;
					buf[1] = (unsigned char)g;
					buf[2] = (unsigned char)r;
				}
				fwrite(buf,3,1,stdout);
			}
		}
	}
}

#include <stdio.h>

void process(int,int,int);
int main()
{
	int buf[3];		//buf[0] : width
				//buf[1] : height
				//buf[2] : format
	fread(buf,4,3,stdin);
	buf[0] /= 2;
	buf[1] /= 2;
	fwrite(buf,4,3,stdout);
	if (buf[2] == 1 || buf[2] == 2 || buf[2] == 5)
		process(buf[0] * 2,buf[1] * 2,4);	//32-bit
	else if (buf[2] == 3)
		process(buf[0] * 2,buf[1] * 2,3);	//24-bit
	else if (buf[2] == 4 || buf[2] == 6 || buf[2] == 7 || buf[2] == 10)
		process(buf[0] * 2,buf[1] * 2,2);	//16-bit
	else if (buf[2] == 8 || buf[2] == 9 || buf[2] == 11)
		process(buf[0] * 2,buf[1] * 2,1);	//8-bit
	else
		fprintf(stderr,"can't process this pixel format #%d\n",buf[2]);
	return 0;
}

void process(int width,int height,int bits)
{
	unsigned char buf[4]="";
	int row,col;
	for (row = 0;row < height;row++)
	{
		for (col = 0;col < width;col++)
		{
			fread(buf,bits,1,stdin);
			if (row % 2 == 0 && col % 2 == 0)
				fwrite(buf,bits,1,stdout);
		}
	}
}

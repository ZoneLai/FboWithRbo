#include "Common.h"
#include <sys/statfs.h>
size_t getFreeSpace(const char* disk){
	size_t freeSpace = 0;
	struct statfs disk_statfs;
	if( statfs(disk, &disk_statfs) >= 0 ){
		freeSpace = disk_statfs.f_bsize  * disk_statfs.f_bfree / 1024;
	}
	return freeSpace;
}
bool isHasEnoughSpace(const char* disk){
    return getFreeSpace(disk)>=RESERVE_SPACE_SIZE;
}


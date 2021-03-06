//
//  LVPackage.m
//  LuaViewSDK
//
//  Created by dongxicheng on 12/30/15.
//  Copyright © 2015 dongxicheng. All rights reserved.
//

#import "LVBundle.h"
#import "LVPkgManager.h"
#import "LVUtil.h"
#import "LVHeads.h"
#import "LView.h"

@interface LVBundle () {
    NSMutableArray *_scriptPaths, *_resourcePaths;
    NSFileManager *_fileManager;
}

@end

@implementation LVBundle

@dynamic resourcePaths, scriptPaths;
@dynamic currentPath;

- (id)init {
    self = [super init];
    if(self) {
        _fileManager = [NSFileManager new];
        [_fileManager changeCurrentDirectoryPath:[[NSBundle mainBundle] resourcePath]];
        
        _resourcePaths = [NSMutableArray arrayWithObjects:[LVUtil PathForCachesResource:nil], @".", nil];
        _scriptPaths = [NSMutableArray arrayWithObject:@"."];
    }
    return self;
}

- (NSString *)currentPath {
    return [_fileManager currentDirectoryPath];
}

- (void)changeCurrentPath:(NSString *)path {
    [_fileManager changeCurrentDirectoryPath:path];
}

- (NSArray *)resourcePaths {
    return [_resourcePaths copy];
}

- (void)addResourcePath:(NSString *)path {
    NSAssert(path, @"path could not be nil");
    
    [_resourcePaths insertObject:path atIndex:0];
}

- (void)removeResourcePath:(NSString *)path {
    NSAssert(path, @"path could not be nil");
    
    [_resourcePaths removeObject:path];
}

- (NSArray *)scriptPaths {
    return [_scriptPaths copy];
}

- (void)addScriptPath:(NSString *)path {
    NSAssert(path, @"path could not be nil");
    
    [_scriptPaths insertObject:path atIndex:0];
}

- (void)removeScriptPath:(NSString *)path {
    NSAssert(path, @"path could not be nil");

    [_scriptPaths removeObject:path];
}

- (NSString *)absolutePath:(NSString *)path {
    if (path == nil) {
        return nil;
    } else if ([path hasPrefix:@"/"]) {
        return path;
    } else {
        return [[_fileManager currentDirectoryPath] stringByAppendingPathComponent:path];
    }
}

- (NSString *)resourcePathWithName:(NSString *)name {
    if (name == nil) {
        return nil;
    }
    
    NSString *fullPath = nil;
    for (NSString *dir in _resourcePaths) {
        fullPath = [dir stringByAppendingPathComponent:name];
        if ([_fileManager fileExistsAtPath:fullPath]) {
            return [self absolutePath:fullPath];
        }
    }
    
    return nil;
}

// TODO: support signed resource
- (NSData *)resourceWithName:(NSString *)name {
    if (name == nil) {
        return nil;
    }
    
    NSString *path = [self resourcePathWithName:name];
    if (path == nil) {
        return nil;
    }
    
    return [_fileManager contentsAtPath:path];
}

- (UIImage *)imageWithName:(NSString *)name {
    if (name == nil) {
        return nil;
    }
    
    NSData *data = [self resourceWithName:name];
    if (data == nil) {
        return nil;
    }
    
    return [UIImage imageWithData:data];
}

- (NSString *)scriptPathWithName:(NSString *)name {
    if (name == nil) {
        return nil;
    }
    
    NSString *ext = [name pathExtension];
    if (ext == nil ||
        !([LVScriptExts[0] isEqualToString:ext] || [LVScriptExts[1] isEqualToString:ext])) {
        
        NSAssert(nil, @"LuaView: %@ file is not supported!", ext);
        return nil;
    }
    
    NSString *fullPath = nil;
    for (NSString *dir in _scriptPaths) {
        fullPath = [dir stringByAppendingPathComponent:name];
        if ([_fileManager fileExistsAtPath:fullPath]) {
            return [self absolutePath:fullPath];
        }
    }
    
    return nil;
}

- (NSData *)scriptWithName:(NSString *)name {
    if (name == nil) {
        return nil;
    }
    NSAssert([[name pathExtension] isEqualToString:LVScriptExts[!LVSignedScriptExtIndex]],
             @"%@ is not normal script", name);
    
    NSString *fullPath = [self scriptPathWithName:name];
    if (fullPath == nil) {
        return nil;
    }
    
    return [LVUtil dataReadFromFile:fullPath];
}

- (NSData *)signedScriptWithName:(NSString *)name rsa:(LVRSA *)rsa {
    if (name == nil) {
        return nil;
    }
    
    NSAssert([[name pathExtension] isEqualToString:LVScriptExts[LVSignedScriptExtIndex]],
             @"%@ is not signed script", name);
    
    NSString *fullPath = [self scriptPathWithName:name];
    if (fullPath == nil) {
        return nil;
    }
    
    return [LVPkgManager readLuaFile:fullPath rsa:rsa];
}

@end

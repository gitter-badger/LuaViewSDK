//
//  LVCustomErrorView.h
//  LVSDK
//
//  Created by dongxicheng on 7/20/15.
//  Copyright (c) 2015 dongxicheng. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LVHeads.h"

@interface LVCustomPanel : UIView<LVProtocal>

@property(nonatomic,weak) LView* lv_lview;
@property(nonatomic,assign) LVUserDataInfo* lv_userData;
@property(nonatomic,assign) CGFloat lv_rotation;
@property(nonatomic,assign) CGFloat lv_rotationX;
@property(nonatomic,assign) CGFloat lv_rotationY;
@property(nonatomic,assign) CGFloat lv_scaleX;
@property(nonatomic,assign) CGFloat lv_scaleY;
@property(nonatomic,assign) NSUInteger lv_align;

+(int) classDefine:(lv_State *)L ;

- (void) callLuaWithArgument:(NSString*) info;

+ (void) addCustomPanel:(Class) c boundName:(NSString*) boundName state:(lv_State*) L;

@end

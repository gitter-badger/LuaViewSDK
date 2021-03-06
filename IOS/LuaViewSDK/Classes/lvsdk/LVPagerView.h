//
//  LVPageView.h
//  LuaViewSDK
//
//  Created by dongxicheng on 9/21/15.
//  Copyright © 2015 dongxicheng. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LVHeads.h"

@class LVPagerIndicator;
@interface LVPagerView : UIScrollView<LVProtocal,UIScrollViewDelegate>


@property(nonatomic,weak) LView* lv_lview;
@property(nonatomic,assign) LVUserDataInfo* lv_userData;
@property(nonatomic,assign) CGFloat lv_rotation;
@property(nonatomic,assign) CGFloat lv_rotationX;
@property(nonatomic,assign) CGFloat lv_rotationY;
@property(nonatomic,assign) CGFloat lv_scaleX;
@property(nonatomic,assign) CGFloat lv_scaleY;
@property(nonatomic,assign) NSUInteger lv_align;


-(id) init:(lv_State*) l;
+(int) classDefine: (lv_State *)L ;

+ (void) setDefaultStyle:(Class) c;

-(void) setIndicator:(LVPagerIndicator*) indicator;
-(void) setCurrentPageIdx:(NSInteger) pageIdx animation:(BOOL) animation;

@end

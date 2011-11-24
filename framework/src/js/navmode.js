(function(){navigationController={SAFE_MARGIN:30,SUPPRESS_NAVIGATION_INPUT_TYPES:"|checkbox|radio|button|",AUTO_FOCUS_INPUT_TYPES:"|color|date|month|time|week|email|number|password|search|text|url|tel|",REQUIRE_CLICK_INPUT_TYPES:"|file|",querySelector:"textarea:not([x-blackberry-focusable=false]),a:not([x-blackberry-focusable=false]),input:not([x-blackberry-focusable=false]),select:not([x-blackberry-focusable=false]),button:not([x-blackberry-focusable=false]),[x-blackberry-focusable=true]",DOWN:3,UP:2,RIGHT:0,LEFT:1,domDirty:false,currentFocused:null,priorFocusedId:"",lastDirection:null,focusableNodes:[],zoomScale:null,currentDirection:null,delta:null,virtualHeight:null,virtualWidth:null,verticalScroll:null,horizontalScroll:null,height:null,width:null,initialize:function(a){navigationController.changeInputNodeTypes(["date","month","time","datetime","datetime-local"]);navigationController.assignScrollData(a);navigationController.focusableNodes=navigationController.getFocusableElements();navigationController.SAFE_MARGIN=navigationController.height/10;if(navigationController.device.isBB5()){addEventListener("DOMNodeInsertedIntoDocument",function(){navigationController.domDirty=true},true);addEventListener("DOMNodeRemovedFromDocument",function(){navigationController.domDirty=true},true)}var b=document.body.querySelectorAll("[x-blackberry-initialFocus=true]");if(b.length===0){navigationController.setRimFocus(navigationController.findHighestFocusableNodeInScreen())}else{var c=b[0];if(!navigationController.isValidFocusableNode(c)){c=null}if(c!==null){var d=navigationController.determineBoundingRect(c);var e={element:c,rect:d.rect,scrollableParent:d.scrollableParent};navigationController.setRimFocus(e)}else{navigationController.setRimFocus(navigationController.findHighestFocusableNodeInScreen())}}},changeInputNodeTypes:function(a){var b,c,d="input[x-blackberry-focusable=true]",e,f;for(b=0;b<a.length;b++){e=d+"[type="+a[b]+"]";f=document.querySelectorAll(e);for(c=0;c<f.length;c++){f[c].type="x-blackberry-"+a[b]}}},device:{isBB5:function(){return navigator.appVersion.indexOf("5.0.0")>=0},isBB6:function(){return navigator.appVersion.indexOf("6.0.0")>=0},isBB7:function(){return navigator.appVersion.indexOf("7.0.0")>=0}},assignScrollData:function(a){navigationController.currentDirection=a.direction;navigationController.delta=a.delta;navigationController.zoomScale=a.zoomScale;navigationController.virtualHeight=a.virtualHeight;navigationController.virtualWidth=a.virtualWidth;navigationController.verticalScroll=a.verticalScroll;navigationController.horizontalScroll=a.horizontalScroll;navigationController.height=a.height;navigationController.width=a.width},getDirection:function(){return navigationController.currentDirection},getFocus:function(){if(navigationController.currentFocused===null){return null}else{return navigationController.currentFocused.element.getAttribute("id")}},setFocus:function(a){if(a.length===0){navigationController.focusOut();return}var b=null;b=document.getElementById(a);if(b!==null){if(!navigationController.isValidFocusableNode(b)){b=null}}if(b!==null){var c=navigationController.determineBoundingRect(b);var d={element:b,rect:c.rect,scrollableParent:c.scrollableParent};navigationController.setRimFocus(d)}},getPriorFocus:function(){return navigationController.priorFocusedId},onScroll:function(data){navigationController.assignScrollData(data);if(!navigationController.device.isBB5()||navigationController.domDirty){navigationController.focusableNodes=navigationController.getFocusableElements();navigationController.domDirty=false}if(navigationController.currentDirection===navigationController.DOWN){if(navigationController.currentFocused&&navigationController.currentFocused.element.hasAttribute("x-blackberry-onDown")){eval(navigationController.currentFocused.element.getAttribute("x-blackberry-onDown"));return}else{navigationController.handleDirectionDown()}}else if(navigationController.currentDirection===navigationController.UP){if(navigationController.currentFocused&&navigationController.currentFocused.element.hasAttribute("x-blackberry-onUp")){eval(navigationController.currentFocused.element.getAttribute("x-blackberry-onUp"));return}else{navigationController.handleDirectionUp()}}else if(navigationController.currentDirection===navigationController.RIGHT){if(navigationController.currentFocused&&navigationController.currentFocused.element.hasAttribute("x-blackberry-onRight")){eval(navigationController.currentFocused.element.getAttribute("x-blackberry-onRight"));return}else{navigationController.handleDirectionRight()}}else if(navigationController.currentDirection===navigationController.LEFT){if(navigationController.currentFocused&&navigationController.currentFocused.element.hasAttribute("x-blackberry-onLeft")){eval(navigationController.currentFocused.element.getAttribute("x-blackberry-onLeft"));return}else{navigationController.handleDirectionLeft()}}navigationController.lastDirection=navigationController.currentDirection},onTrackpadDown:function(){},onTrackpadUp:function(){if(navigationController.currentFocused===null){return}try{var a=document.createEvent("MouseEvents");a.initMouseEvent("mouseup",true,true,window,0,navigationController.currentFocused.rect.x,navigationController.currentFocused.rect.y,1,1,false,false,false,false,0,null);navigationController.currentFocused.element.dispatchEvent(a);navigationController.onTrackpadClick()}catch(b){}},onTrackpadClick:function(){if(!navigationController.currentFocused){return}var a=navigationController.currentFocused,b=document.createEvent("MouseEvents"),c,d;b.initMouseEvent("click",true,true,window,0,0,0,0,0,false,false,false,false,0,null);c=!a.element.dispatchEvent(b);if(!c){if(typeof (navigationController[a.element.tagName]==="function")){navigationController[a.element.tagName](a.element)}}},INPUT:function(a){navigationController.onDATETIME=function(b){var c=document.createEvent("HTMLEvents"),d=false;if(a.value!==b){a.value=b;d=true}if(d){c.initEvent("change",true,true);a.dispatchEvent(c)}};var b=a.attributes.getNamedItem("type").value;switch(b){case"x-blackberry-date":case"x-blackberry-datetime":case"x-blackberry-datetime-local":case"x-blackberry-month":case"x-blackberry-time":navigationController.handleInputDateTime(b.substring(b.lastIndexOf("-")+1),{value:a.value,min:a.min,max:a.max,step:a.step},"navigationController.onDATETIME");break;default:break}},SELECT:function(a){function b(a){var b=[],c=a.options,d=0,e,f="",g;for(d;d<c.length;d++){e=c.item(d);g=e.parentNode.tagName==="OPTGROUP"?e.parentNode.label:"";if(f!==g){f=g;b.push({label:f,enabled:false,selected:false,type:"group"})}b.push({label:e.text,enabled:e.disabled||e.disabled==false,selected:e.selected||e.selected==true,type:"option"})}return b}navigationController.onSELECT=function(b){var c,d=document.createEvent("HTMLEvents"),e=[],f=false;for(c=0;c<a.options.length;c++){e.push(false)}for(c=0;c<b.length;c++){e[b[c]]=true}for(c=0;c<e.length;c++){if(e[c]!==a.options.item(c).selected){a.options.item(c).selected=e[c];f=true}}if(f){d.initEvent("change",true,true);a.dispatchEvent(d)}};navigationController.handleSelect(typeof a.attributes.multiple!=="undefined"?true:false,b(a),"navigationController.onSELECT")},indexOf:function(a){var b=navigationController.focusableNodes.length;for(var c=0;c<b;c++){if(navigationController.focusableNodes[c].element==a.element)return c}return-1},handleDirectionDown:function(){var a=navigationController.getUnscaledScreenRect();var b=navigationController.findDownFocusableNode();if(b!=null){var c=b.rect;if(c.y<=a.y+a.height){navigationController.setRimFocus(b);return}}var d=navigationController.unscaleValue(navigationController.virtualHeight)-a.y-a.height;if(d>navigationController.SAFE_MARGIN){d=navigationController.SAFE_MARGIN}if(d>0){if(navigationController.currentFocused!=null){var e=navigationController.currentFocused.rect;if(e.y+e.height<=a.y+d){navigationController.focusOut()}}navigationController.scrollDown()}},handleDirectionUp:function(){var a=navigationController.getUnscaledScreenRect();var b=navigationController.findUpFocusableNode();if(b!=null){var c=b.rect;if(c.y+c.height>a.y){navigationController.setRimFocus(b);return}}var d=a.y;if(d>navigationController.SAFE_MARGIN){d=navigationController.SAFE_MARGIN}if(d>0){if(navigationController.currentFocused!=null){var e=navigationController.currentFocused.rect;if(e.y>a.y-d+a.height){navigationController.focusOut()}}navigationController.scrollUp()}},handleDirectionRight:function(){var a=navigationController.getUnscaledScreenRect();var b=navigationController.findRightFocusableNode();if(b!=null){var c=b.rect;if(c.x<=a.x+a.width){navigationController.setRimFocus(b);return}}var d=navigationController.unscaleValue(navigationController.virtualWidth)-a.x-a.width;if(d>navigationController.SAFE_MARGIN){d=navigationController.SAFE_MARGIN}if(d>0){if(navigationController.currentFocused!=null){var e=navigationController.currentFocused.rect;if(e.x+e.width<=a.x+d){navigationController.focusOut()}}navigationController.scrollRight()}},handleDirectionLeft:function(){var a=navigationController.getUnscaledScreenRect();var b=navigationController.findLeftFocusableNode();if(b!=null){var c=b.rect;if(c.x+c.width>a.x){navigationController.setRimFocus(b);return}}var d=a.x;if(d>navigationController.SAFE_MARGIN){d=navigationController.SAFE_MARGIN}if(d>0){if(navigationController.currentFocused!=null){var e=navigationController.currentFocused.rect;if(e.x>a.x-d+a.width){navigationController.focusOut()}}navigationController.scrollLeft()}},findHighestFocusableNodeInScreen:function(){if(navigationController.focusableNodes==null||navigationController.focusableNodes.length==0)return null;var a=navigationController.getUnscaledScreenRect();var b=null;var c=null;var d=navigationController.focusableNodes.length;for(var e=0;e<d;e++){var f=navigationController.focusableNodes[e];var g=f.rect;if(g==null||g.width==0||g.height==0){continue}if(navigationController.isRectIntersectingVertically(g,a)){var h=false;if(g.y>=a.y){h=navigationController.needSwapWithDownRect(c,g)}if(h){b=f;c=g}}}return b},findLowestFocusableNodeInScreen:function(){if(navigationController.focusableNodes==null||navigationController.focusableNodes.length==0)return null;var a=navigationController.getUnscaledScreenRect();var b=null;var c=null;var d=navigationController.focusableNodes.length;for(var e=d-1;e>=0;e--){var f=navigationController.focusableNodes[e];var g=f.rect;if(g==null||g.width==0||g.height==0){continue}if(navigationController.isRectIntersectingVertically(g,a)){var h=false;if(g.y+g.height<a.y+a.height){h=navigationController.needSwapWithUpRect(c,g)}if(h){b=f;c=g}}}return b},findLeftmostFocusableNodeInScreen:function(){if(navigationController.focusableNodes==null||navigationController.focusableNodes.length==0)return null;var a=navigationController.getUnscaledScreenRect();var b=null;var c=null;var d=navigationController.focusableNodes.length;for(var e=d-1;e>=0;e--){var f=navigationController.focusableNodes[e];var g=f.rect;if(g==null||g.width==0||g.height==0){continue}if(navigationController.isRectIntersectingHorizontally(g,a)){var h=false;if(g.x<a.x+a.width){if(b==null){h=true}else{if(g.x==c.x){if(g.width>c.width){h=true}}else if(g.x>c.x){h=true}}}if(h){b=f;c=g}}}return b},findRightmostFocusableNodeInScreen:function(){if(navigationController.focusableNodes==null||navigationController.focusableNodes.length==0)return null;var a=navigationController.getUnscaledScreenRect();var b=null;var c=null;var d=navigationController.focusableNodes.length;for(var e=0;e<d;e++){var f=navigationController.focusableNodes[e];var g=f.rect;if(g==null||g.width==0||g.height==0){continue}if(navigationController.isRectIntersectingHorizontally(g,a)){var h=false;if(g.x>=a.x){if(b==null){h=true}else{if(g.x==c.x){if(g.width<c.width){h=true}}else if(g.x<c.x){h=true}}}if(h){b=f;c=g}}}return b},findDownFocusableNode:function(){if(navigationController.focusableNodes==null||navigationController.focusableNodes.length==0)return null;var a;if(navigationController.currentFocused!=null)a=navigationController.indexOf(navigationController.currentFocused);else return navigationController.findHighestFocusableNodeInScreen();if(a==-1){return navigationController.findHighestFocusableNodeInScreen()}var b=navigationController.currentFocused.rect;var c=navigationController.getUnscaledScreenRect();var d=navigationController.focusableNodes.length;var e=null;var f=null;for(var g=0;g<d;g++){if(g==a){continue}var h=navigationController.focusableNodes[g];var i=h.rect;if(i==null||i.width==0||i.height==0){continue}if(navigationController.isRectIntersectingVertically(i,b)){var j=false;if(i.y==b.y){if(i.height==b.height){if(g>a){return h}}else if(i.height>b.height){j=navigationController.needSwapWithDownRectInPriority(f,i)}}else if(i.y>b.y){j=navigationController.needSwapWithDownRectInPriority(f,i)}if(j){e=h;f=i}}else if(!navigationController.isRectIntersectingHorizontally(i,b)&&navigationController.isRectIntersectingVertically(i,c)){var j=false;if(i.y>b.y){j=navigationController.needSwapWithDownRect(f,i)}if(j){e=h;f=i}}}return e},findUpFocusableNode:function(){if(navigationController.focusableNodes==null||navigationController.focusableNodes.length==0)return null;var a;if(navigationController.currentFocused!=null)a=navigationController.indexOf(navigationController.currentFocused);else return navigationController.findLowestFocusableNodeInScreen();if(a==-1){return navigationController.findLowestFocusableNodeInScreen()}var b=navigationController.currentFocused.rect;var c=null;var d=null;var e=navigationController.getUnscaledScreenRect();var f=navigationController.focusableNodes.length;for(var g=f-1;g>=0;g--){if(g==a){continue}var h=navigationController.focusableNodes[g];var i=h.rect;if(i==null||i.width==0||i.height==0){continue}if(navigationController.isRectIntersectingVertically(i,b)){var j=false;if(i.y==b.y){if(i.height==b.height){if(g<a){return h}}else if(i.height<b.height){j=navigationController.needSwapWithUpRectInPriority(d,i)}}else if(i.y<b.y){j=navigationController.needSwapWithUpRectInPriority(d,i)}if(j){c=h;d=i}}else if(!navigationController.isRectIntersectingHorizontally(i,b)&&navigationController.isRectIntersectingVertically(i,e)){var j=false;if(i.y<b.y){j=navigationController.needSwapWithUpRect(d,i)}if(j){c=h;d=i}}}return c},findLeftFocusableNode:function(){if(navigationController.focusableNodes==null||navigationController.focusableNodes.length==0)return null;var a;if(navigationController.currentFocused!=null)a=navigationController.indexOf(navigationController.currentFocused);else return navigationController.findLeftmostFocusableNodeInScreen();if(a==-1){return navigationController.findLeftmostFocusableNodeInScreen()}var b=navigationController.currentFocused.rect;var c=null;var d=null;var e=navigationController.focusableNodes.length;for(var f=e-1;f>=0;f--){if(f==a){continue}var g=navigationController.focusableNodes[f];var h=g.rect;if(h==null||h.width==0||h.height==0){continue}if(navigationController.isRectIntersectingHorizontally(h,b)){var i=false;if(h.x==b.x){if(h.width==b.width){if(f<a){return g}}else if(h.width<b.width){if(c==null){i=true}else{if(h.x==d.x){if(h.width>d.width){i=true}}else if(h.x>d.x){i=true}}}}else if(h.x<b.x){if(c==null){i=true}else{if(h.x==d.x){if(h.width>d.width){i=true}}else if(h.x>d.x){i=true}}}if(i){c=g;d=h}}}return c},findRightFocusableNode:function(){if(navigationController.focusableNodes==null||navigationController.focusableNodes.length==0)return null;var a;if(navigationController.currentFocused!=null)a=navigationController.indexOf(navigationController.currentFocused);else return navigationController.findRightmostFocusableNodeInScreen();if(a==-1){return navigationController.findRightmostFocusableNodeInScreen()}var b=navigationController.currentFocused.rect;var c=null;var d=null;var e=navigationController.focusableNodes.length;for(var f=0;f<e;f++){if(f==a){continue}var g=navigationController.focusableNodes[f];var h=g.rect;if(h==null||h.width==0||h.height==0){continue}if(navigationController.isRectIntersectingHorizontally(h,b)){var i=false;if(h.x==b.x){if(h.width==b.width){if(f>a){return g}}else if(h.width>b.width){if(c==null){i=true}else{if(h.x==d.x){if(h.width<d.width){i=true}}else if(h.x<d.x){i=true}}}}else if(h.x>b.x){if(c==null){i=true}else{if(h.x==d.x){if(h.width<d.width){i=true}}else if(h.x<d.x){i=true}}}if(i){c=g;d=h}}}return c},getFocusableElements:function(){var a=document.body.querySelectorAll(navigationController.querySelector);var b=a.length;var c=[];for(var d=0;d<b;d++){var e=a[d];var f=navigationController.determineBoundingRect(e);var g={element:e,rect:f.rect,scrollableParent:f.scrollableParent};g.element.addEventListener("mouseover",function(a){var b=navigationController.focusableNodes.length;for(var c=0;c<b;c++){if(this==navigationController.focusableNodes[c].element){navigationController.currentFocused=navigationController.focusableNodes[c]}}},false);c.push(g)}return c},determineBoundingRect:function(a){var b=0;var c=0;var d=a.offsetHeight;var e=a.offsetWidth;var f=null;if(a.offsetParent){do{b+=a.offsetTop;c+=a.offsetLeft;if(f==null&&a.parentNode!=null&&a.parentNode.style.overflow=="scroll"){f=a.parentNode}if(a.style.position=="absolute"||a.style.position=="fixed")break;if(!a.offsetParent)break}while(a=a.offsetParent)}return{scrollableParent:f,rect:{y:b,x:c,height:d,width:e}}},setRimFocus:function(a){try{navigationController.focusOut();var b=document.createEvent("MouseEvents");b.initMouseEvent("mouseover",true,true,window,0,a.rect.x,a.rect.y,1,1,false,false,false,false,null,null);a.element.dispatchEvent(b);navigationController.currentFocused=a;if(navigationController.isAutoFocus(a)){a.element.focus()}navigationController.scrollToRect(navigationController.scaleRect(a.rect))}catch(c){console.log(c);console.log(c.message)}},focusOut:function(){if(navigationController.currentFocused!=null){var a=navigationController.currentFocused;navigationController.priorFocusedId=a.element.getAttribute("id");var b=document.createEvent("MouseEvents");b.initMouseEvent("mouseout",true,true,window,0,navigationController.currentFocused.rect.x,navigationController.currentFocused.rect.y,1,1,false,false,false,false,null,null);navigationController.currentFocused.element.dispatchEvent(b);navigationController.currentFocused=null;if(navigationController.isAutoFocus(a)){a.element.blur()}}},isRectIntersectingVertically:function(a,b){if(a==null||b==null)return false;if(b.x<=a.x&&b.x+b.width-1>=a.x)return true;return b.x>=a.x&&b.x<=a.x+a.width-1},needSwapWithDownRectInPriority:function(a,b){if(a==null)return true;if(b.y==a.y&&b.height<=a.height)return true;return b.y<a.y},isRectIntersectingHorizontally:function(a,b){if(a==null||b==null)return false;if(b.y<=a.y&&b.y+b.height-1>=a.y)return true;return b.y>=a.y&&b.y<=a.y+a.height-1},needSwapWithDownRect:function(a,b){if(a==null)return true;if(b.y==a.y&&b.height<a.height)return true;return b.y<a.y},needSwapWithUpRectInPriority:function(a,b){if(a==null)return true;if(b.y==a.y&&b.height>=a.height)return true;return b.y>a.y},needSwapWithUpRect:function(a,b){if(a==null)return true;if(b.y==a.y&&b.height>a.height)return true;return b.y>a.y},getUnscaledScreenRect:function(){var a={y:navigationController.verticalScroll,x:navigationController.horizontalScroll,height:navigationController.height,width:navigationController.width};return navigationController.unscaleRect(a)},isValidFocusableNode:function(a){if(a==null)return false;if(navigationController.indexOf({element:a})==-1){return false}var b=navigationController.determineBoundingRect(a);return!(b==null||b.width==0||b.height==0)},isAutoFocus:function(a){if(a.element.tagName=="INPUT"){var b=a.element.getAttribute("type").toLowerCase();return navigationController.AUTO_FOCUS_INPUT_TYPES.indexOf(b)>0}return false},scrollToRect:function(a){var b=navigationController.verticalScroll;var c=b;if(a.y<b){c=Math.max(a.y,0)}else if(a.y+a.height>b+navigationController.height){c=Math.min(a.y+a.height-navigationController.height,navigationController.virtualHeight-navigationController.height)}if(c-b!=0){navigationController.scrollY(c)}var d=navigationController.horizontalScroll;var e=d;if(a.width>=navigationController.width){e=Math.max(a.x,0)}else if(a.x<d){e=Math.max(a.x,0)}else if(a.x+a.width>d+navigationController.width){e=Math.min(a.x+a.width-navigationController.width,navigationController.virtualWidth-navigationController.width)}if(e-d!=0){navigationController.scrollX(e)}},scrollDown:function(){var a=Math.min(navigationController.verticalScroll+navigationController.scaleValue(navigationController.SAFE_MARGIN),navigationController.virtualHeight-navigationController.height);navigationController.scrollY(a)},scrollUp:function(){var a=Math.max(navigationController.verticalScroll-navigationController.scaleValue(navigationController.SAFE_MARGIN),0);navigationController.scrollY(a)},scrollRight:function(){var a=Math.min(navigationController.horizontalScroll+navigationController.scaleValue(navigationController.SAFE_MARGIN),navigationController.virtualWidth-navigationController.width);navigationController.scrollX(a)},scrollLeft:function(){var a=Math.max(navigationController.horizontalScroll-navigationController.scaleValue(navigationController.SAFE_MARGIN),0);navigationController.scrollX(a)},scaleRect:function(a){var b={y:navigationController.scaleValue(a.y),x:navigationController.scaleValue(a.x),height:navigationController.scaleValue(a.height),width:navigationController.scaleValue(a.width)};return b},scaleValue:function(a){return Math.round(a*navigationController.zoomScale)},unscaleValue:function(a){return Math.round(a/navigationController.zoomScale)},unscaleRect:function(a){var b={y:navigationController.unscaleValue(a.y),x:navigationController.unscaleValue(a.x),height:navigationController.unscaleValue(a.height),width:navigationController.unscaleValue(a.width)};return b},scrollX:function(a){window.scrollTo(a,window.pageYOffset)},scrollY:function(a){window.scrollTo(window.pageXOffset,a)}};bbNav={init:function(){var a={direction:3,delta:1,zoomScale:1,virtualHeight:screen.height,virtualWidth:screen.width,verticalScroll:0,horizontalScroll:0,height:screen.height,width:screen.width};blackberry.focus.onScroll=navigationController.onScroll;blackberry.focus.onTrackpadDown=navigationController.onTrackpadDown;blackberry.focus.onTrackpadUp=navigationController.onTrackpadUp;blackberry.focus.getDirection=navigationController.getDirection;blackberry.focus.getFocus=navigationController.getFocus;blackberry.focus.getPriorFocus=navigationController.getPriorFocus;blackberry.focus.setFocus=navigationController.setFocus;blackberry.focus.focusOut=navigationController.focusOut;navigationController.initialize(a);navigationController.handleSelect=blackberry.ui.dialog.selectAsync;navigationController.handleInputDateTime=blackberry.ui.dialog.dateTimeAsync}};addEventListener("DOMContentLoaded",bbNav.init,false)})()
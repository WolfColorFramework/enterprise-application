(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-064aa1b8"],{"3a33":function(e,t,i){"use strict";i.r(t);i("8e6e"),i("456d"),i("55dd"),i("ac6a"),i("7f7f"),i("6b54"),i("386d");var n=i("ade3"),o=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"premission-set-dialog"},[i("el-dialog",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],attrs:{title:e.title,visible:e.visible,"custom-class":"premission-set-dialog",fullscreen:""},on:{"update:visible":function(t){e.visible=t},close:e.close}},[i("div",{staticClass:"ctrls-div"},[i("el-row",{attrs:{gutter:10}},e._l(e.rowGroup,(function(t,n){return i("el-col",{key:n,attrs:{span:6}},e._l(t.list,(function(t){return i("el-card",{key:t,staticClass:"box-card",staticStyle:{"margin-bottom":"10px"}},[i("div",{staticClass:"clearfix",staticStyle:{position:"relative"},attrs:{slot:"header"},slot:"header"},[i("el-checkbox",{on:{change:function(i){return e.onHeaderCheckChange(t)}},model:{value:t._checked,callback:function(i){e.$set(t,"_checked",i)},expression:"item._checked"}},[i("i",{class:t.icon}),e._v(" "+e._s(t.name)+"\n                ")]),i("el-button-group",{staticStyle:{position:"absolute",right:"-15px",top:"-6px"}},[i("el-button",{attrs:{size:"small"},on:{click:function(i){return e.toggleExpand(t)}}},[e._v(e._s(t._expand?e.$t("REDUCE"):e.$t("EXPAND")))]),i("el-button",{attrs:{size:"small"},on:{click:function(e){t._minisize=!t._minisize}}},[e._v(e._s(t._minisize?e.$t("DISPLAY"):e.$t("HIDE")))])],1)],1),i("permission-tree",{directives:[{name:"show",rawName:"v-show",value:!t._minisize,expression:"!item._minisize"}],ref:"tree_"+t.id,refInFor:!0,attrs:{"node-key":"id",data:t.children,"default-checked-keys":t._checkedKeys},on:{"check-change":function(i){return e.onTreeCheckChange(i,t)}}}),i("span",{directives:[{name:"show",rawName:"v-show",value:t._minisize,expression:"item._minisize"}]},[e._v(e._s(e.$t("CONTENT HIDING")))])],1)})),1)})),1)],1),i("el-row",{staticStyle:{"margin-top":"20px"},attrs:{gutter:0}},[i("el-col",{attrs:{span:12}},[i("el-button-group",[i("el-button",{attrs:{size:"small"},on:{click:function(t){return e.selectAll(!0)}}},[e._v(e._s(e.$t("SELECT ALL")))]),i("el-button",{attrs:{size:"small"},on:{click:function(t){return e.selectAll(!1)}}},[e._v(e._s(e.$t("CLEAR")))])],1),i("el-button-group",[i("el-button",{attrs:{size:"small"},on:{click:function(t){return e.expandAll(!1)}}},[e._v(e._s(e.$t("REDUCE")))]),i("el-button",{attrs:{size:"small"},on:{click:function(t){return e.expandAll(!0)}}},[e._v(e._s(e.$t("EXPAND")))])],1),i("el-button-group",[i("el-button",{attrs:{size:"small"},on:{click:function(t){return e.miniSizeAll(!0)}}},[e._v(e._s(e.$t("HIDE")))]),i("el-button",{attrs:{size:"small"},on:{click:function(t){return e.miniSizeAll(!1)}}},[e._v(e._s(e.$t("DISPLAY")))])],1)],1),i("el-col",{staticStyle:{"text-align":"right"},attrs:{span:12}},[i("el-button",{attrs:{size:"small",icon:"fa fa-close"},on:{click:function(t){e.visible=!1}}},[e._v(e._s(e.$t("CANCEL")))]),i("el-button",{attrs:{size:"small",type:"primary",icon:"fa fa-check"},on:{click:e.submit}},[e._v(e._s(e.$t("OK")))])],1)],1)],1)],1)},r=[],l={name:"",props:{visible:{type:Boolean,default:!1}},data:function(){return{data:[],loading:!1,roleId:"",succBack:null,title:this.$t("PERMISSION CONFIGURATION"),rowGroup:[{list:[],count:0},{list:[],count:0},{list:[],count:0},{list:[],count:0}]}},methods:{show:function(e,t,i){var n=this;n.visible=!0,n.roleId=e,n.succBack=i,n.title=t?this.$t("PERMISSION CONFIGURATION")+" - "+t:t,n.loading=!0,RequestManager.getRolePrimission(e,(function(e){e.tree.forEach((function(t){t._checked=-1!=e.ids.indexOf(t.id),t._expand=!0,t._minisize=!1,t._checkedKeys=n.getItemCheckedKeys(t,e.ids)})),n.data=e.tree,n.checks=e.ids,n.loading=!1,n.setGroups()}),$global.commonRequestError)},setGroups:function(){var e=this;e.data.forEach((function(t){var i=e.getMiniCountGroup();i.list.push(t),i.count+=e.getCount(t)}))},getCount:function(e){var t=this;if(!e.children)return 0;var i=e.children.length;return e.children&&e.children.length>0&&e.children.forEach((function(e){i+=t.getCount(e)})),i},getMiniCountGroup:function(){var e=null,t=this;return t.rowGroup.forEach((function(t){(!e||e.count>t.count)&&(e=t)})),e},getItemCheckedKeys:function(e,t){var i=[];function n(e){-1!=t.indexOf(e.id)&&i.push(e.id);var o=e.children;if(o&&!(o.length<1))for(var r=0;r<o.length;r++){var l=o[r];n(l)}}return n(e),i},onHeaderCheckChange:function(e){var t=this.$refs["tree_"+e.id];t&&t[0]&&t[0].checkAll(e._checked)},close:function(){this.data=[],this.loading=!1,this.roleId="",this.succBack=null,this.rowGroup=[{list:[],count:0},{list:[],count:0},{list:[],count:0},{list:[],count:0}]},onTreeCheckChange:function(e,t){e&&e.checked&&(t._checked=!0)},toggleExpand:function(e,t){var i=this.$refs["tree_"+e.id];e._expand=void 0===t?!e._expand:t,e._expand&&(e._minisize=!1),i&&i[0]&&i[0].expandAll(e._expand)},selectAll:function(e){var t=this;this.data.forEach((function(i){var n=t.$refs["tree_"+i.id];i._checked=e,n&&n[0]&&n[0].checkAll(i._checked)}))},expandAll:function(e){var t=this;this.data.forEach((function(i){t.toggleExpand(i,e)}))},miniSizeAll:function(e){this.data.forEach((function(t){t._minisize=e}))},getCheckIds:function(){var e=this,t=[];return this.data.forEach((function(i){i._checked&&t.push(i.id);var n=e.$refs["tree_"+i.id],o=n&&n[0]?n[0].getCheckedNodes():null;o&&o.forEach((function(e){t.push(e.id)}))})),t},submit:function(){var e=this;this.loading=!0;var t=e.getCheckIds();RequestManager.setRolePrimission(e.roleId,t,(function(){$global.succTip(this.$t("SUCCESSFUL PERMISSION CONFIGURATION")),e.visible=!1,e.succBack&&(e.succBack(),e.succBack=null)}),$global.commonRequestError),this.$emit("submitSuccess")}}},a=l,s=i("2877"),c=Object(s["a"])(a,o,r,!1,null,"a5c82dce",null),d=c.exports,u=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"permission-tree"},[i("el-tree",{ref:"tree",attrs:{"check-strictly":"","check-on-click-node":"","default-expand-all":e.allExpand,data:e.data,"default-checked-keys":e.defaultCheckedKeys,"node-key":e.nodeKey,"show-checkbox":"","filter-node-method":e.filterNodeMethod},on:{"check-change":e.onCheckChange,check:e.onCheckEnd},scopedSlots:e._u([{key:"default",fn:function(t){var n=t.node,o=t.data;return i("span",{staticClass:"custom-tree-node",class:(0==o.type?"menu-node":"btn-node")+" level-"+o.level},[i("span",{staticClass:"custom-tree-label"},[i("i",{class:0==o.type?e.menuIcon:e.moduleIcon}),i("span",{staticClass:"custom-tree-label-text"},[e._v(e._s(n.label))])])])}}])})],1)},h=[],f={name:"permission-tree",props:{data:{type:Array,default:function(){return[]}},defaultCheckedKeys:{type:Array,default:function(){return[]}},nodeKey:{type:String,default:function(){return"id"}},filterNodeMethod:{type:Function,default:function(){return null}},menuIcon:{type:String,default:function(){return"el-icon-starring-caidan"}},moduleIcon:{type:String,default:function(){return"el-icon-starring-multiplicationbox"}}},data:function(){return{_checkChanging:!1,_checkingAll:!1,allExpand:!0}},methods:{getCheckedNodes:function(){return this.$refs.tree.getCheckedNodes()},filter:function(e){return this.$refs.tree.filter(e)},setChildrenChecked:function(e,t){var i=this;e.children&&e.children.length>0&&e.children.forEach((function(e){i.$refs.tree.setChecked(e,t,!1),i.setChildrenChecked(e,t)}))},getParentNode:function(e,t){for(var i=0;i<e.length;i++){var n=e[i];if(n.children&&!(n.children.length<1)){if(-1!==n.children.indexOf(t))return n;var o=this.getParentNode(n.children,t);if(o)return o}}return null},setParentChecked:function(e,t){var i=this.getParentNode(this.data,e);i&&(this.$refs.tree.setChecked(i,t,!1),this.setParentChecked(i,t))},onCheckChange:function(e,t,i){this._checkChanging||this._checkingAll||(this._checkChanging=!0,this.setChildrenChecked(e,t),t&&this.setParentChecked(e,!0),this.$emit("check-change",{data:e,checked:t}))},onCheckEnd:function(){this._checkChanging=!1,this._checkingAll=!1},checkAll:function(e){var t=this;function i(n){t.$refs.tree.setChecked(n,e,!1),n.children&&n.children.length>0&&n.children.forEach((function(e){i(e)}))}t._checkingAll=!0,t.data.forEach((function(e){i(e)})),setTimeout((function(){t._checkingAll=!1,t._checkChanging=!1}),100)},expandAll:function(e){for(var t=0;t<this.$refs.tree.store._getAllNodes().length;t++)this.$refs.tree.store._getAllNodes()[t].expanded=e}}},g=f,p=Object(s["a"])(g,u,h,!1,null,"36e46ce4",null),m=p.exports,b=i("89ce"),v=i("cc5e"),k=i("b578");function C(e,t){var i=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),i.push.apply(i,n)}return i}function y(e){for(var t=1;t<arguments.length;t++){var i=null!=arguments[t]?arguments[t]:{};t%2?C(Object(i),!0).forEach((function(t){Object(n["a"])(e,t,i[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(i)):C(Object(i)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(i,t))}))}return e}t["default"]={components:{PremissionSetDialog:d,PermissionTree:m,baseDialog:b["a"]},data:function(){return{permissionVisible:!1,search:{departIds:[],name:"",departTree:[]},table:{multipleSelection:[],sort:{properties:"createTime",direction:"desc"},data:{list:[],pageSize:10,currentPage:1,total:1}},roleDialog:{title:"title",visible:!1,formDefault:{id:"",name:"",departId:"",remark:"",departIds:[]},form:y({},this.formDefault),rules:{name:[{required:!0,validator:function(e,t,i){return""===t.trim()?i(new Error("请输入角色名称")):/^[^ ]+$/.test(t)?void i():i(new Error("角色名称不能含有空格"))},trigger:"blur"}],departId:[{required:!0,message:"请选择角色所在部门",trigger:"blur"}]},formSubmit:function(){},departProps:{value:"id",label:"label"},departTree:[]},permissionDialog:{visible:!1,roleId:"",permissionTree:[],permissionChecked:[]},permissionFilter:"",del_popover_visible:!1}},computed:{roleDialogDepartId:{get:function(){return this.roleDialog.form.departIds},set:function(e){this.roleDialog.form.departIds=e,e.length>0?this.roleDialog.form.departId=e[e.length-1]:this.roleDialog.form.departId="",this.$refs.roleForm.validateField("departId")}}},created:function(){this.loadDepart(),this.refreshList(),this.getRoleList(),this.getDepartmentTree()},methods:{getRoleList:function(){var e=this,t=1===this.search.departIds.length?this.search.departIds.toString():"";Object(v["e"])({page:this.table.data.currentPage,size:this.table.data.pageSize,condition:{departmentId:t,name:this.search.name}}).then((function(t){var i=t.data;e.table.data.list=i.rows,e.table.data.total=i.totalSize})).catch((function(e){return console.log(e)}))},changeTree:function(e){function t(t){return e.apply(this,arguments)}return t.toString=function(){return e.toString()},t}((function(e){var t=[];return e.forEach((function(e){t.push({uuid:e.uuid,id:e.uuid,label:e.name}),e.children&&e.children.length>0&&(e.children=changeTree(e.children))})),t})),getDepartmentTree:function(){var e=this;Object(k["c"])().then((function(t){var i=t.data;e.roleDialog.departTree=e.changeTree(i),e.search.departTree=e.changeTree(i)}))},loadDepart:function(){},onTableSelectChange:function(e){var t=this;console.log(e),t.table.multipleSelection=e},onPageChange:function(e){this.table.data.currentPage=e,this.getRoleList()},roleDialogSubmit:function(){console.log("aallaa",this.roleDialog.form);var e=this;function t(t){if(t){var i={};if(i.name=e.roleDialog.form.name,i.remark=e.roleDialog.form.remark,""===e.roleDialog.form.id)i.departmentId=e.roleDialog.form.departIds.join(),Object(v["a"])(i).then((function(t){console.log("新增角色",t),e.$message.success("新增角色成功"),e.getRoleList()})).catch((function(e){return console.log(e)})).finally((function(){e.roleDialog.visible=!1}));else{i.departmentId=e.roleDialog.form.departIds;e.roleDialog.form.id;i.uuid=e.roleDialog.form.id,i.createTime=e.roleDialog.form.createTime,Object(v["f"])(i).then((function(t){console.log("data22",t),e.$message.success("修改角色成功"),e.getRoleList()})).catch((function(e){return console.log(e)})).finally((function(){e.roleDialog.visible=!1}))}}}this.$refs.roleForm.validate(t)},permissionFilterFunc:function(e,t){return!e||-1!==t.label.indexOf(e)},searchHandler:function(){this.table.data.currentPage=1,this.getRoleList()},permissionSubmit:function(){var e=this,t=(this.roleId,[]),i=this.$refs.primissionTree.getCheckedNodes();i.forEach((function(e){t.push(e.id)})),0!==t.length||e.$message({message:"请分配权限",type:"warning",center:!0})},onPermissionDialogClose:function(){this.permissionTree=[],this.permissionChecked=[],this.permissionFilter=""},handleSizeChange:function(e){this.table.data.currentPage=1,this.table.data.pageSize=e,this.getRoleList()},refreshList:function(){var e={page:this.table.data.currentPage,size:this.table.data.pageSize};this.table.sort&&(e.properties=this.table.sort.properties,e.direction=this.table.sort.direction);var t={};this.search.name&&(t.name=this.search.name);var i=this.search.departIds;i&&i.length>0&&(t.departId=i[i.length-1])},btnClickHandler:function(e){var t=this,i=this;switch(e){case"add":this.$refs.roleForm&&this.$refs.roleForm.resetFields(),this.roleDialog.title="添加角色",this.roleDialog.form=y({},this.roleDialog.formDefault),this.roleDialog.visible=!0;break;case"edit":if(1!==this.table.multipleSelection.length)return void this.$message.warning("请选择一条数据");this.$refs.roleForm&&this.$refs.roleForm.resetFields(),this.roleDialog.title="编辑角色",this.roleDialog.form=y({},this.roleDialog.formDefault),this.roleDialog.visible=!0;var n=this.table.multipleSelection[0];Object(v["d"])(n.uuid).then((function(e){var t=e.data;i.roleDialog.form.id=t.uuid,i.roleDialog.form.name=t.name,i.roleDialog.form.departId=t.departmentId,i.roleDialog.form.remark=t.remark,i.roleDialog.form.createTime=t.createTime,i.roleDialogDepartId=t.departmentId}));break;case"delete":var o=this.getSelectRoleIds();if(o)for(var r=0;r<o.length;r++)if("00000000000000000000000000000001"===o[r])return void i.$message({message:"不能删除超级管理员",type:"warning",center:!0});this.$confirm("此操作将删除选中的角色, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){Object(v["b"])(o.join(",")).then((function(e){console.log("删除结果",e),t.$message.success("删除成功"),i.table.data.currentPage=1,t.getRoleList()})).catch((function(e){return console.log(e)}))})).catch((function(){}));break;case"role_set":if(console.log(this.table.multipleSelection),1!==this.table.multipleSelection.length)return void this.$message.warning("请选择一条数据");n=this.table.multipleSelection[0];console.log("row",n),this.$refs.premissionSetDialog.show(n.id,n.name),this.permissionVisible=!0,this.$nextTick((function(){}));break}},getSelectRole:function(){var e=this.table.multipleSelection;return console.log(e),e},getSelectRoleIds:function(){var e=this.table.multipleSelection,t=[];return e.forEach((function(e){t.push(e.uuid)})),t},formatCreateTime:function(e,t){var i=new Date;return i.setTime(e.createTime),i.format("yyyy-MM-dd hh:mm:ss")},formatUpdateTime:function(e,t){var i=new Date;return i.setTime(e.updateTime),i.format("yyyy-MM-dd hh:mm:ss")},onTableRowClick:function(e,t,i){this.$refs.multipleTable.toggleRowSelection(e)},onTableSortChange:function(e){this.table.sort={},this.table.sort.properties=e.prop,"departName"===this.table.sort.properties&&(this.table.sort.properties="departId"),this.table.sort.direction="ascending"===e.order?"asc":"desc",this.refreshList()}},watch:{permissionFilter:function(e){console.log(e),e=e.trim(),this.$refs.primissionTree.filter(e)}},destroyed:function(){}}},"89ce":function(e,t,i){"use strict";var n=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("el-dialog",{ref:"selfDialog",staticClass:"mit-el-dialog",class:e.title?"":"mit-el-dialog-notitle",attrs:{title:e.title,visible:e.dialogVisible,top:e.dialogTop,width:e.width,"close-on-press-escape":e.closeOnPressEscape,"close-on-click-modal":e.closeOnClickModal,fullscreen:e.fullscreen,"custom-class":e.customClass,"append-to-body":e.appendToBody,"modal-append-to-body":e.modalAppendToBody,"lock-scroll":e.lockScroll,"show-close":e.showClose,"before-close":e.beforeClose,center:e.center},on:{"update:visible":function(t){e.dialogVisible=t},open:e.onOpen,opened:e.onOpened,close:e.onClose,closed:e.onClosed}},[e._t("default"),e._t("footer",null,{slot:"footer"})],2)},o=[],r={name:"baseDialog",components:{},data:function(){return{dialogTop:"0px",resizeDelay:0,resizeBind:!1,resizeInterval:0,dialogWidth:0,dialogHeight:0}},props:{title:{type:String,default:""},visible:{type:Boolean,default:!1},width:{type:String,default:"60%"},closeOnPressEscape:{type:Boolean,default:!1},closeOnClickModal:{type:Boolean,default:!1},fullscreen:{type:Boolean,default:!1},customClass:{type:String,default:""},appendToBody:{type:Boolean,default:!1},modalAppendToBody:{type:Boolean,default:!0},lockScroll:{type:Boolean,default:!0},showClose:{type:Boolean,default:!0},beforeClose:{type:Function,default:null},center:{type:Boolean,default:!1}},computed:{dialogVisible:{get:function(){return this.visible},set:function(e){this.$emit("update:visible",e)}}},methods:{onOpen:function(){this.$emit("open")},onOpened:function(){this.$emit("opened")},onClose:function(){this.$emit("close"),clearInterval(this.resizeInterval),window.removeEventListener("resize",this.windowResizeHandler),this.resizeBind=!1,this.dialogVisible=!1,this.resizeInterval=0,this.dialogWidth=0,this.dialogHeight=0},onClosed:function(){this.$emit("closed")},updateDialogTop:function(e){if(this.dialogVisible&&this.$refs.selfDialog&&this.$refs.selfDialog.$el.children&&this.$refs.selfDialog.$el.children[0]){var t=this.$refs.selfDialog.$el;if(t&&t.children){var i=t.children[0];if(i){var n=i.children;if(n){for(var o=0,r=0;r<n.length;r++){var l=n[r];o+=l.clientHeight||l.offsetHeight}var a=.4*(document.body.clientHeight-o);a<0&&(a=0),this.dialogTop=a+"px",e&&(i.style.marginTop=this.dialogTop)}}}}},windowResizeHandler:function(){var e=this;clearTimeout(e.resizeDelay),console.log("resize on window"),e.resizeDelay=setTimeout((function(){e.updateDialogTop(!0)}),100)},resizeTimerHandler:function(){var e=this;if(this.$refs.selfDialog){var t=this.$refs.selfDialog.$el.offsetWidth,i=this.$refs.selfDialog.$el.offsetHeight;if(0===e.dialogWidth&&0===e.dialogWidth)return e.dialogWidth=t,void(e.dialogWidth=i);if(Math.abs(e.dialogWidth-t<10)&&Math.abs(e.dialogWidth-i<10))return;e.dialogWidth=t,e.dialogWidth=i,clearTimeout(e.resizeDelay),e.updateDialogTop(!0)}},initUpdateTop:function(){this.dialogVisible&&(this.fullscreen||(this.resizeBind||(this.resizeBind=!0,window.addEventListener("resize",this.windowResizeHandler)),0===this.resizeInterval&&(this.dialogWidth=0,this.dialogHeight=0,this.resizeInterval=setInterval(this.resizeTimerHandler,100)),this.updateDialogTop()))}},mounted:function(){},updated:function(){this.initUpdateTop()}},l=r,a=i("2877"),s=Object(a["a"])(l,n,o,!1,null,null,null);t["a"]=s.exports},b578:function(e,t,i){"use strict";i.d(t,"a",(function(){return o})),i.d(t,"e",(function(){return r})),i.d(t,"b",(function(){return l})),i.d(t,"d",(function(){return a})),i.d(t,"c",(function(){return s}));var n=i("b775");function o(e){return Object(n["a"])({url:"/department/add",method:"post",data:e})}function r(e){return Object(n["a"])({url:"/department/update",method:"post",data:e})}function l(e){return Object(n["a"])({url:"/department/delete",method:"post",data:{departmentIds:e}})}function a(e){return Object(n["a"])({url:"/department/query/".concat(e),method:"get"})}function s(){return Object(n["a"])({url:"/department/tree",method:"get"})}},cc5e:function(e,t,i){"use strict";i.d(t,"a",(function(){return o})),i.d(t,"f",(function(){return r})),i.d(t,"d",(function(){return l})),i.d(t,"e",(function(){return a})),i.d(t,"c",(function(){return s})),i.d(t,"b",(function(){return c}));var n=i("b775");function o(e){return Object(n["a"])({url:"role/add",method:"post",data:e})}function r(e){return Object(n["a"])({url:"/role/update",method:"post",data:e})}function l(e){return Object(n["a"])({url:"/role/query/".concat(e),method:"get"})}function a(e){return Object(n["a"])({url:"/role/page",method:"post",data:e})}function s(){return Object(n["a"])({url:"/role/list",method:"get"})}function c(e){return Object(n["a"])({url:"/role/delete",method:"post",data:{roleIds:e}})}}}]);
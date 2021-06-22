module chat_app_module {
	requires java.base;
	requires java.desktop;
	requires chat_lib_module;
	
	exports chat_app_pack;
}
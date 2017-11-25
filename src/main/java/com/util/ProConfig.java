package com.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(value = "classpath:file/conf.properties")
@Component
public class ProConfig {
	private static String token_url;
	private static String queryOrder_url;
	private static String blocks_url;
	private static String fabricexplorer_url;
	private static String createFp_url;
	private static String commonkey;
	private static String jfsc_eth_url;
	private static String jfsc_diff_path;
	private static String jfsc_server_ip;
	private static String jfsc_server_port;
	private static String jfsc_server_username;
	private static String jfsc_server_passwd;
	private static String jfsc_genesis_file_linux_path;
	private static String jfsc_genesis_file_win_path;

	public static String getToken_url() {
		return token_url;
	}

	public static void setToken_url(String token_url) {
		ProConfig.token_url = token_url;
	}

	public static String getQueryOrder_url() {
		return queryOrder_url;
	}

	public static void setQueryOrder_url(String queryOrder_url) {
		ProConfig.queryOrder_url = queryOrder_url;
	}

	public static String getBlocks_url() {
		return blocks_url;
	}

	public static void setBlocks_url(String blocks_url) {
		ProConfig.blocks_url = blocks_url;
	}

	public static String getFabricexplorer_url() {
		return fabricexplorer_url;
	}

	public static void setFabricexplorer_url(String fabricexplorer_url) {
		ProConfig.fabricexplorer_url = fabricexplorer_url;
	}

	public static String getCreateFp_url() {
		return createFp_url;
	}

	public static void setCreateFp_url(String createFp_url) {
		ProConfig.createFp_url = createFp_url;
	}

	public static String getCommonkey() {
		return commonkey;
	}

	public static void setCommonkey(String commonkey) {
		ProConfig.commonkey = commonkey;
	}

	public static String getJfsc_eth_url() {
		return jfsc_eth_url;
	}

	public static void setJfsc_eth_url(String jfsc_eth_url) {
		ProConfig.jfsc_eth_url = jfsc_eth_url;
	}

	public static String getJfsc_diff_path() {
		return jfsc_diff_path;
	}

	public static void setJfsc_diff_path(String jfsc_diff_path) {
		ProConfig.jfsc_diff_path = jfsc_diff_path;
	}

	public static String getJfsc_server_ip() {
		return jfsc_server_ip;
	}

	public static void setJfsc_server_ip(String jfsc_server_ip) {
		ProConfig.jfsc_server_ip = jfsc_server_ip;
	}

	public static String getJfsc_server_port() {
		return jfsc_server_port;
	}

	public static void setJfsc_server_port(String jfsc_server_port) {
		ProConfig.jfsc_server_port = jfsc_server_port;
	}

	public static String getJfsc_server_username() {
		return jfsc_server_username;
	}

	public static void setJfsc_server_username(String jfsc_server_username) {
		ProConfig.jfsc_server_username = jfsc_server_username;
	}

	public static String getJfsc_server_passwd() {
		return jfsc_server_passwd;
	}

	public static void setJfsc_server_passwd(String jfsc_server_passwd) {
		ProConfig.jfsc_server_passwd = jfsc_server_passwd;
	}

	public static String getJfsc_genesis_file_linux_path() {
		return jfsc_genesis_file_linux_path;
	}

	public static void setJfsc_genesis_file_linux_path(String jfsc_genesis_file_linux_path) {
		ProConfig.jfsc_genesis_file_linux_path = jfsc_genesis_file_linux_path;
	}

	public static String getJfsc_genesis_file_win_path() {
		return jfsc_genesis_file_win_path;
	}

	public static void setJfsc_genesis_file_win_path(String jfsc_genesis_file_win_path) {
		ProConfig.jfsc_genesis_file_win_path = jfsc_genesis_file_win_path;
	}

}

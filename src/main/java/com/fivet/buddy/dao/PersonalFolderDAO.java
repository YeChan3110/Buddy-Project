package com.fivet.buddy.dao;

import com.fivet.buddy.dto.PersonalFolderDTO;
import com.fivet.buddy.mapper.PersonalFolderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.mail.Folder;
import java.util.List;
import java.util.Map;

@Repository
public class PersonalFolderDAO {

    @Autowired
    private PersonalFolderMapper personalFolderMapper;


    // (개인)회원 별 폴더 불러오기
    public List<PersonalFolderDTO> selectMyFolders(Map<String,Object> map) throws Exception{
        return personalFolderMapper.selectMyFolders(map);
    }

    // 폴더 생성
    public void insertNewFolder(PersonalFolderDTO personalFolderDto) throws Exception{
        personalFolderMapper.insertNewFolder(personalFolderDto);
    }

    // 폴더 존재 유무 체크
    public boolean isFolderExists(Map<String, Object> folderCheck) throws Exception{
        return personalFolderMapper.isFolderExists(folderCheck);
    }

    // 회원가입 시 personal foler 테이블에 기본폴더 생성
    public void newPersonalFolder(Map<String, Object> map) throws Exception{
        personalFolderMapper.newPersonalFolder(map);
    }

    // 부모 폴더 경로 찾아오기(폴더 생성)
    public String searchPath(String parentKey) throws Exception{
        return personalFolderMapper.searchPath(parentKey);
    }

    // personal_folder 테이블에서 내 기본 폴더key 추출
    public String myBasicFolder(int memberSeq) throws Exception{
        return personalFolderMapper.myBasicFolder(memberSeq);
    }

    public List<PersonalFolderDTO> selectChildFolders(String resourceKey) {
        return personalFolderMapper.selectChildFolders(resourceKey);
    }

    // 현재 폴더 이름
    public String nowFolder(String resourceKey) {
        return personalFolderMapper.nowFolder(resourceKey);
    }

    // 폴더 삭제
    public void deleteFolder(List<Map<String, String>> folders) {
        personalFolderMapper.deleteFolder(folders);
    }

    // 현재 폴더 경로
    public String myPath(String key) {
        return personalFolderMapper.myPath(key);
    }

    // 폴더 삭제(경로)
    public void deleteFolderByPath(String path) {
        personalFolderMapper.deleteFolderByPath(path);
    }

    // 폴더 밑에 하위폴더가 있는지 확인
    public boolean subIsExist(String folderKey) {
        return personalFolderMapper.subIsExist(folderKey);
    }
}

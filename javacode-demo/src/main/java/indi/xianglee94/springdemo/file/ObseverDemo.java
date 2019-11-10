package indi.xianglee94.javacode.file;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;

public class ObseverDemo {

    public static void main(String[] args) {
        try {
            //构造观察类主要提供要观察的文件或目录，当然还有详细信息的filter  
            FileAlterationObserver observer = new FileAlterationObserver(new File("G:\\Log.log"), new FileFilterImpl());
            //构造收听类 没啥好说的  
            FileListenerAdaptor listener = new FileListenerAdaptor();
            //为观察对象添加收听对象  
            observer.addListener(listener);
            //配置Monitor，第一个参数单位是毫秒，是监听的间隔；第二个参数就是绑定我们之前的观察对象。  
            FileAlterationMonitor fileMonitor = new FileAlterationMonitor(5000, new FileAlterationObserver[]{observer});
            //启动开始监听  
            fileMonitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



class FileListenerAdaptor extends FileAlterationListenerAdaptor {
    @Override
    public void onStart(FileAlterationObserver arg0) {
        System.out.println("begin listening!");
        super.onStart(arg0);
    }

    @Override
    public void onStop(FileAlterationObserver arg0) {
        System.out.println("end listening!");
        super.onStop(arg0);
    }

    @Override
    public void onDirectoryCreate(File fold) {
        System.out.println("fold: " + fold.getAbsolutePath() + " is created.");
        super.onDirectoryCreate(fold);
    }

    @Override
    public void onDirectoryChange(File fold) {
        System.out.println("fold: " + fold.getAbsolutePath() + " is changed.");
        super.onDirectoryChange(fold);
    }

    @Override
    public void onDirectoryDelete(File fold) {
        System.out.println("fold: " + fold.getAbsolutePath() + " is deleted.");
        super.onDirectoryDelete(fold);
    }

    @Override
    public void onFileCreate(File file) {
        System.out.println("file: " + file.getAbsolutePath() + " is created.");
        super.onFileCreate(file);
    }

    @Override
    public void onFileChange(File file) {
        System.out.println("file: " + file.getAbsolutePath() + " is changed.");
        super.onFileChange(file);
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("file: " + file.getAbsolutePath() + " is deleted");
        super.onFileDelete(file);
    }
}


class FileFilterImpl implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        System.out.println(pathname);
        return false;
    }
}

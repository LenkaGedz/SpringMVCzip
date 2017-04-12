package ua.kiev.prog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/")
public class MyController {

    private Map<Long, byte[]> bytes = new HashMap<Long, byte[]>();
    private Map<Long, String> files = new HashMap<Long, String>();
    private File fileZip = new File("D:\\files.zip");

    @RequestMapping("/")
    public String onIndex(Model model) {
        model.addAttribute("files", files);
        return "index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String onAddFile(Model model, @RequestParam MultipartFile[] fileList) {
        for(MultipartFile file : fileList) {
            if (file.isEmpty())
                throw new FileErrorException();
            try {
                long id = System.currentTimeMillis();
                bytes.put(id, file.getBytes());
                files.put(id, file.getOriginalFilename());
            } catch (IOException e) {
                throw new FileErrorException();
            }
        }
        model.addAttribute("files", files);
        return "index";
    }

    @RequestMapping(value = "/toZip")
    public String toZip(Model model, HttpServletResponse response) {
        if (bytes.size() < 1) {
            return "index";
        }
        Long [] fileList = bytes.keySet().toArray(new Long[bytes.size()]);
        zipFiles(fileList);
        try {
            FileInputStream is = new FileInputStream(fileZip);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileZip.getName() + "\"");
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[is.available()];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            response.flushBuffer();
            is.close();
            os.close();
        } catch (IOException ex) {
            throw new FileErrorException();
        }
        model.addAttribute("files", files);
        return "index";
    }

    private void zipFiles(Long[] fileList){
        byte[] buf = new byte[1024];
        try {
            FileOutputStream fos = new FileOutputStream(fileZip);
            ZipOutputStream out = new ZipOutputStream(fos);
            for (Long idd : fileList) {
                ByteArrayInputStream sourceStream = new ByteArrayInputStream(bytes.get(idd));
                out.putNextEntry(new ZipEntry(files.get(idd)));
                int len;
                while ((len = sourceStream.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
            }
            out.flush();
            out.close();
            fos.close();
        } catch (IOException e) {
            throw new FileErrorException();
        }
    }

    @RequestMapping(value = "/clean", method = RequestMethod.POST)
    public String onClean(Model model) {
        bytes.clear();
        files.clear();
        fileZip.delete();
        return "index";
    }

}

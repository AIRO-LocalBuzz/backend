package backend.airo.application.clure_fatvl.port;

import backend.airo.infra.open_api.clure_fatvl.vo.ClutrFatvlInfo;

import java.util.List;

public interface ClutrFatvlPort {

    List<ClutrFatvlInfo> getClureFatvlInfo(String startDate);

}

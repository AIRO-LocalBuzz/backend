package backend.airo.batch.rural_ex;

import backend.airo.support.TimeCatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuralService {


    private final TimeCatch timeCatch = new TimeCatch("RuralService Time Check");

}

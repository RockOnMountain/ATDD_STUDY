package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.line.acceptance.LineSteps;
import nextstep.subway.path.domain.PathType;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PathUtils {

    private PathUtils() {
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, PathType type) {
        RequestSpecification requestSpecification = RestAssured.given().log().all();
        Map<String, Object> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        params.put("type", type.name());

        return 두_역의_경로_조회를_요청(requestSpecification, params);
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(RequestSpecification requestSpecification, Map<String, Object> params) {
        return requestSpecification.accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(params)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 경로_조회_성공(ExtractableResponse<Response> response, List<Long> stations) {
        Assertions.assertAll(() -> {
            assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactlyElementsOf(stations);
            assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
            assertThat(response.jsonPath().getInt("duration")).isEqualTo(5);
        });
    }

    public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");

        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    public static Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }
}
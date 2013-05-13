package com.baidu.soushang.cloudapis;

import android.content.Context;
import it.restrung.rest.client.ContextAwareAPIDelegate;
import it.restrung.rest.client.RestClientFactory;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class Apis {
	public interface ApiResponseCallback<T extends AbstractJSONResponse> {
		public void onResults(T arg0);
		public void onError(Throwable arg0);
	}
	public static void getNextQuestion(Context context, final ApiResponseCallback<QuestionResponse> callback) {
		RestClientFactory.getClient().getAsync(new ContextAwareAPIDelegate<QuestionResponse>(context, QuestionResponse.class) {

			@Override
			public void onError(Throwable arg0) {
				if (callback != null) {
					callback.onError(arg0);
				}
			}

			@Override
			public void onResults(QuestionResponse arg0) {
				if (callback != null) {
					callback.onResults(arg0);
				}
			}
		}, "http://soushang.limijiaoyin.com/index.php/Devent/next/s/0.html", 2 * 1000);
	}
}

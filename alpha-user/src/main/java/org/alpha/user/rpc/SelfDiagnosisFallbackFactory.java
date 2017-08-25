package org.alpha.user.rpc;

import org.springframework.stereotype.Component;

import feign.hystrix.FallbackFactory;

@Component
public class SelfDiagnosisFallbackFactory implements FallbackFactory<SelfDiagnosisFeign>  {

	@Override
	public SelfDiagnosisFeign create(Throwable e) {
		return new SelfDiagnosisFeign() {
			@Override
			public String getUserInfo() {
				// TODO Auto-generated method stub
				return "fallback cause of " +e.getMessage();
			}
		};
	}

}
